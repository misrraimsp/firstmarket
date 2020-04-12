package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.FormPassword;
import misrraimsp.uned.pfg.firstmarket.adt.dto.FormUser;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.event.*;
import misrraimsp.uned.pfg.firstmarket.event.security.SecurityEvent;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;

@Controller
@Validated
public class UserController implements Constants {

    private UserServer userServer;
    private PasswordEncoder passwordEncoder;
    private CatServer catServer;
    private ApplicationEventPublisher applicationEventPublisher;
    private MessageSource messageSource;

    @Autowired
    public UserController(UserServer userServer,
                          PasswordEncoder passwordEncoder,
                          CatServer catServer,
                          ApplicationEventPublisher applicationEventPublisher,
                          MessageSource messageSource) {

        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
        this.catServer = catServer;
        this.applicationEventPublisher = applicationEventPublisher;
        this.messageSource = messageSource;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("formUser", new FormUser());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("emailPattern", EMAIL);
        model.addAttribute("textBasicPattern", TEXT_BASIC);
        model.addAttribute("passwordPattern", PASSWORD);
        return "newUser";
    }

    @PostMapping("/newUser")
    public String processNewUser(@Valid FormUser formUser, Errors errors, Model model) {
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
        else if (userServer.emailExists(formUser.getEmail())) { // check email uniqueness
            User user = (User) userServer.loadUserByUsername(formUser.getEmail());
            if (user.isSuspended()){
                isRestarting = true;
            }
            else {
                hasError = true;
                errors.rejectValue("email", "email.notUnique");
            }
        }
        if (hasError) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }
        // complete process
        try {
            SecurityEvent securityEvent;
            Long userId;
            if (isRestarting) {
                securityEvent = SecurityEvent.RESTART_USER;
                userId = ((User) userServer.loadUserByUsername(formUser.getEmail())).getId();
            }
            else {
                securityEvent = SecurityEvent.NEW_USER;
                userId = userServer.persist(formUser, passwordEncoder, null, null).getId();
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
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("emailPattern", EMAIL);
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
            model.addAttribute("emailPattern", EMAIL);
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
            model.addAttribute("emailPattern", EMAIL);
            return "resetPassword";
        }
        // authenticated users are not allowed to trigger the reset password process
        return "redirect:/home";
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam String email) {
        try {
            User user = (User) userServer.loadUserByUsername(email);
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
            model.addAttribute("formPassword", new FormPassword());
            model.addAttribute("passwordPattern", PASSWORD);
            return "editPassword";
        }
        return "redirect:/home";
    }

    @PostMapping("/editPassword")
    public String processEditPassword(@Valid FormPassword formPassword,
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
            else if (!userServer.checkPassword(authUser.getId(),passwordEncoder, formPassword.getCurrentPassword())){
                hasError = true;
                errors.rejectValue("currentPassword", "password.invalid");
            }
            if (hasError) {
                User user = userServer.findById(authUser.getId());
                model.addAttribute("firstName", user.getProfile().getFirstName());
                model.addAttribute("cartSize", user.getCart().getCartSize());;
                model.addAttribute("mainCategories", catServer.getMainCategories());
                model.addAttribute("passwordPattern", PASSWORD);
                return "editPassword";
            }
            // edit password
            userServer.editPassword(authUser.getId(), passwordEncoder, formPassword.getPassword());
        }
        return "redirect:/home";
    }

    @GetMapping("/user/editProfile")
    public String showEditProfileForm(Model model, @AuthenticationPrincipal User authUser){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("profile", user.getProfile());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("textBasicPattern", TEXT_BASIC);
        return "editProfile";
    }

    @PostMapping("/user/editProfile")
    public String processEditProfile(@Valid Profile profile,
                                     Errors errors,
                                     Model model,
                                     @AuthenticationPrincipal User authUser) {
        if (errors.hasErrors()) {
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            return "editProfile";
        }
        userServer.editProfile(authUser.getId(), profile);
        return "redirect:/home";
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
        model.addAttribute("textLongPattern", TEXT_LONG);
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
            model.addAttribute("textLongPattern", TEXT_LONG);
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
