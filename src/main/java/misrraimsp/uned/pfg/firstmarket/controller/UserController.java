package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.PasswordForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.ProfileForm;
import misrraimsp.uned.pfg.firstmarket.adt.dto.UserForm;
import misrraimsp.uned.pfg.firstmarket.config.propertyHolder.ValidationRegexProperties;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.SecurityEvent;
import misrraimsp.uned.pfg.firstmarket.event.*;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.SecurityToken;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.UserDeletion;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@Controller
public class UserController {

    private UserServer userServer;
    private PasswordEncoder passwordEncoder;
    private CatServer catServer;
    private ApplicationEventPublisher applicationEventPublisher;
    private MessageSource messageSource;
    private ValidationRegexProperties validationRegexProperties;

    @Autowired
    public UserController(UserServer userServer,
                          PasswordEncoder passwordEncoder,
                          CatServer catServer,
                          ApplicationEventPublisher applicationEventPublisher,
                          MessageSource messageSource,
                          ValidationRegexProperties validationRegexProperties) {

        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
        this.catServer = catServer;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
        this.validationRegexProperties = validationRegexProperties;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("patterns", validationRegexProperties);
        return "newUser";
    }

    @PostMapping("/newUser")
    public String processNewUser(@Valid UserForm userForm, Errors errors, Model model) {
        // error checks
        boolean isRestarting = false;
        boolean hasError = false;
        if (errors.hasErrors()) {
            hasError = true;
            if (errors.hasGlobalErrors()){
                for (ObjectError objectError : errors.getGlobalErrors()){
                    if (objectError.getCode().equals("PasswordMatches")){
                        errors.rejectValue("matchingPassword", "password.notMatching", objectError.getDefaultMessage());
                    }
                    else { // TODO log this state
                        System.out.println(objectError);
                    }
                }
            }
        }
        // manage email-already-exists situations
        else if (userServer.emailExists(userForm.getEmail())) {
            User user = userServer.getUserByEmail(userForm.getEmail());
            // user is suspended (the user has deleted his account)
            if (user.isSuspended()){
                // there is a valid restart_user token sent waiting for email confirmation
                if (userServer.isEmailConfirmationAlreadyNeededFor(user.getId(),SecurityEvent.RESTART_USER)){
                    hasError = true;
                    errors.rejectValue("email", "email.checkEmail");
                }
                // normal restoring workflow
                else {
                    isRestarting = true;
                }
            }
            // user is not completed -and not suspended- (email address verification has never occurred)
            else if (!user.isCompleted()) {
                // there is a valid new_user token sent waiting for email confirmation
                if (userServer.isEmailConfirmationAlreadyNeededFor(user.getId(),SecurityEvent.NEW_USER)){
                    hasError = true;
                    errors.rejectValue("email", "email.checkEmail");
                }
                // user is not complete but the new_user token has expired, so the user and the token are deleted and
                // normal new user workflow goes on
                else {
                    userServer.garbageCollection();
                }
            }
            // user is completed and not suspended, so that email can not be used again
            else {
                hasError = true;
                errors.rejectValue("email", "email.notUnique");
            }
        }
        if (hasError) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("patterns", validationRegexProperties);
            return "newUser";
        }
        // complete process
        try {
            SecurityEvent securityEvent;
            Long userId;
            if (isRestarting) {
                securityEvent = SecurityEvent.RESTART_USER;
                userId = userServer.getUserByEmail(userForm.getEmail()).getId();
            }
            else {
                securityEvent = SecurityEvent.NEW_USER;
                userId = userServer.persist(userForm, passwordEncoder, null, null).getId();
            }
            // trigger email confirmation
            applicationEventPublisher.publishEvent(
                    new OnEmailConfirmationNeededEvent(securityEvent, userId, null)
            );
            return "redirect:/emailConfirmationRequest";
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending at /newUser");
            me.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/user/editEmail")
    public String showEditEmailForm(Model model, @AuthenticationPrincipal User authUser) {
        loadModel(model, authUser);
        return "editEmail";
    }

    @PostMapping("/user/editEmail")
    public String processEditEmail(@RequestParam String password,
                                   @RequestParam String editedEmail,
                                   Model model,
                                   @AuthenticationPrincipal User authUser) {

        // error checks
        boolean hasError = false;
        String errorMessage = null;
        if (!userServer.checkPassword(authUser.getId(), passwordEncoder, password)) { // check password
            hasError = true;
            errorMessage = messageSource.getMessage("password.invalid", null, null);
        }
        else if (userServer.emailExists(editedEmail)) { // check email uniqueness
            hasError = true;
            errorMessage = messageSource.getMessage("email.notUnique", null, null);
        }
        if (hasError) {
            User user = userServer.findById(authUser.getId());
            model.addAttribute("message", errorMessage);
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("patterns", validationRegexProperties);
            return "editEmail";
        }
        // trigger email confirmation
        try {
            applicationEventPublisher.publishEvent(
                    new OnEmailConfirmationNeededEvent(SecurityEvent.EMAIL_CHANGE, authUser.getId(), editedEmail)
            );
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending");
        }
        return "redirect:/emailConfirmationRequest";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordForm(Model model, @AuthenticationPrincipal User authUser) {
        if (authUser == null) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("patterns", validationRegexProperties);
            return "resetPassword";
        }
        // authenticated users are not allowed to trigger the reset password process
        return "redirect:/home";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam String email) {
        try {
            User user = userServer.getUserByEmail(email);
            applicationEventPublisher.publishEvent(
                    new OnEmailConfirmationNeededEvent(SecurityEvent.RESET_PASSWORD, user.getId(), null)
            );
            return "redirect:/emailConfirmationRequest";
        }
        catch (UsernameNotFoundException e) { // catch email not found
            return "redirect:/emailConfirmationRequest";
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending at /resetPassword");
            me.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/emailConfirmationRequest")
    public String showEmailConfirmationRequest(Model model, @AuthenticationPrincipal User authUser){
        if (authUser != null) {
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "emailConfirmationRequest";
    }

    @GetMapping("/emailConfirmation")
    public String processEmailConfirmation(@RequestParam("token") String token,
                                           Model model,
                                           @AuthenticationPrincipal User authUser){

        // error checks
        SecurityToken securityToken = userServer.getSecurityToken(token);
        boolean hasError = false;
        String errorMessage = null;
        if (securityToken == null) { // check for invalid token
            hasError = true;
            errorMessage = messageSource.getMessage("auth.invalidToken", null, null);
        }
        else if ((securityToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) { // check for expired token
            hasError = true;
            errorMessage = messageSource.getMessage("auth.expiredToken", null, null);
        }
        if (hasError) {
            if (authUser != null) {
                User user = userServer.findById(authUser.getId());
                model.addAttribute("firstName", user.getProfile().getFirstName());
                model.addAttribute("cartSize", user.getCart().getCartSize());
            }
            model.addAttribute("message", errorMessage);
            model.addAttribute("mainCategories", catServer.getMainCategories());
            return "emailConfirmationError";
        }
        // complete confirmation
        try {
            switch (securityToken.getSecurityEvent()){
                case NEW_USER:
                    userServer.setCompletedState(securityToken.getUser().getId(),true);
                    userServer.deleteSecurityToken(securityToken.getId());
                    applicationEventPublisher.publishEvent(
                            new OnNewUserEvent(securityToken.getUser().getId())
                    );
                    return "redirect:/login";
                case RESTART_USER:
                    userServer.setSuspendedState(securityToken.getUser().getId(),false);
                    userServer.deleteSecurityToken(securityToken.getId());
                    applicationEventPublisher.publishEvent(
                            new OnRestartUserEvent(securityToken.getUser().getId())
                    );
                    return "redirect:/login";
                case EMAIL_CHANGE:
                    userServer.editEmail(securityToken.getUser().getId(), securityToken.getEditedEmail());
                    userServer.deleteSecurityToken(securityToken.getId());
                    applicationEventPublisher.publishEvent(
                            new OnEmailEditionEvent(securityToken.getUser().getId())
                    );
                    return "redirect:/home";
                case RESET_PASSWORD:
                    String randomPassword = userServer.getRandomPassword();
                    applicationEventPublisher.publishEvent(
                            new OnResetPasswordEvent(securityToken.getUser().getId(), randomPassword)
                    );
                    userServer.editPassword(securityToken.getUser().getId(), passwordEncoder, randomPassword);
                    userServer.deleteSecurityToken(securityToken.getId());
                    return "resetPasswordConfirmation";
                default:
                    return "redirect:/home";
            }
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending at /emailConfirmation");
            me.printStackTrace();
            return "redirect:/home";
        }
    }

    @GetMapping("/editPassword")
    public String showEditPasswordForm(Model model, @AuthenticationPrincipal User authUser){
        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("passwordForm", new PasswordForm());
            model.addAttribute("patterns", validationRegexProperties);
            return "editPassword";
        }
        return "redirect:/home";
    }

    @PostMapping("/editPassword")
    public String processEditPassword(@Valid PasswordForm passwordForm,
                                      Errors errors,
                                      Model model,
                                      @AuthenticationPrincipal User authUser) {

        if (authUser != null){
            // error checks
            boolean hasError = false;
            if (errors.hasErrors()) {
                hasError = true;
                if (errors.hasGlobalErrors()){
                    for (ObjectError objectError : errors.getGlobalErrors()){
                        if (objectError.getCode().equals("PasswordMatches")){
                            errors.rejectValue("matchingPassword", "password.notMatching", objectError.getDefaultMessage());
                        }
                        else{//debug TODO log this situation
                            System.out.println(objectError);
                        }
                    }
                }
            }
            else if (!userServer.checkPassword(authUser.getId(),passwordEncoder, passwordForm.getCurrentPassword())){
                hasError = true;
                errors.rejectValue("currentPassword", "password.invalid");
            }
            if (hasError) {
                loadModel(model, authUser);
                return "editPassword";
            }
            // edit password
            userServer.editPassword(authUser.getId(), passwordEncoder, passwordForm.getPassword());
        }
        return "redirect:/home";
    }

    @GetMapping("/user/profileForm")
    public String showProfileForm(Model model, @AuthenticationPrincipal User authUser){
        try {
            model.addAttribute("profileForm", userServer.getProfileForm(authUser.getId()));
            loadModel(model, authUser);
        } catch (IllegalArgumentException e){
            //TODO log this situation
            return "redirect:/home";
        }
        return "profileForm";
    }

    @PostMapping("/user/profileForm")
    public String processProfileForm(@Valid ProfileForm profileForm,
                                     Errors errors,
                                     Model model,
                                     @AuthenticationPrincipal User authUser) {

        if (errors.hasErrors()) {
            loadModel(model, authUser);
            return "profileForm";
        }
        try {
            Profile profile = userServer.convertProfileFormToProfile(profileForm);
            userServer.editProfile(authUser.getId(), profile);
        } catch (IllegalArgumentException e) {
            //TODO log this situation
            return "redirect:/home";
        }
        return "redirect:/home";
    }

    private void loadModel(Model model, @AuthenticationPrincipal User authUser) throws IllegalArgumentException {
        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("patterns", validationRegexProperties);
    }

    @GetMapping("/user/cart")
    public String showCart(@AuthenticationPrincipal User authUser, Model model){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("items", user.getCart().getItems());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "cart";
    }

    @GetMapping("/user/purchases")
    public String showPurchases(@AuthenticationPrincipal User authUser, Model model){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("purchases", user.getPurchases());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "purchases";
    }

    @GetMapping("/user/addPurchase")
    public String processAddPurchase(@AuthenticationPrincipal User authUser){
        userServer.addPurchase(authUser.getId());
        return "redirect:/home";
    }

    @GetMapping("/user/deleteUser")
    public String showDeleteUserForm(Model model, @AuthenticationPrincipal User authUser) {
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("deletionReasons", DeletionReason.values());
        model.addAttribute("patterns", validationRegexProperties);
        return "deleteUser";
    }

    //TODO validate comment input
    @PostMapping("/user/deleteUser")
    public String processDeleteUser(@RequestParam(required = false) String deletionReason,
                                    @RequestParam(required = false) String comment,
                                    @RequestParam String password,
                                    Model model,
                                    @AuthenticationPrincipal User authUser,
                                    HttpServletRequest request) {

        User user = userServer.findById(authUser.getId());
        // error checks
        boolean hasError = false;
        String errorMessage = null;
        if (!userServer.checkPassword(authUser.getId(), passwordEncoder, password)) { // check password
            hasError = true;
            errorMessage = messageSource.getMessage("password.invalid", null, null);
        }
        if (hasError) {
            model.addAttribute("message", errorMessage);
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("deletionReasons", DeletionReason.values());
            model.addAttribute("patterns", validationRegexProperties);
            return "deleteUser";
        }
        // complete deletion
        userServer.setSuspendedState(authUser.getId(),true);
        UserDeletion userDeletion = userServer.createUserDeletion(authUser.getId(), deletionReason, comment);
        //logout
        new SecurityContextLogoutHandler().logout(request, null, null);
        // trigger user removal event
        try {
            applicationEventPublisher.publishEvent(new OnDeleteUserEvent(userDeletion));
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending at /user/deleteUser");
            me.printStackTrace();
        }
        return "redirect:/home";
    }

}
