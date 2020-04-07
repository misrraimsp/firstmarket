package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.FormPassword;
import misrraimsp.uned.pfg.firstmarket.adt.dto.FormUser;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.event.OnEmailConfirmationNeededEvent;
import misrraimsp.uned.pfg.firstmarket.exception.EmailAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.exception.InvalidPasswordException;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.VerificationToken;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Calendar;

@Controller
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
        User registeredUser = null;
        // check validation errors
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
        // try to persist, catching email non-uniqueness condition
        else {
            try {
                registeredUser = userServer.persist(formUser, passwordEncoder, null, null);
            }
            catch (EmailAlreadyExistsException e) {
                hasError = true;
                errors.rejectValue("email", "email.notUnique");
            }
        }
        // manage error situation
        if (hasError) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }
        // trigger email verification
        try {
            applicationEventPublisher.publishEvent(new OnEmailConfirmationNeededEvent(registeredUser, null));
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending");
            me.printStackTrace();
        }
        return "redirect:/emailConfirmationRequest";
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
        User user = userServer.findById(authUser.getId());
        boolean hasError = false;
        String errorMessage = null;
        if (!userServer.checkPassword(user, passwordEncoder, password)) { // check password
            hasError = true;
            errorMessage = messageSource.getMessage("password.invalid", null, null);
        }
        else if (userServer.emailExists(editedEmail)) { // check email uniqueness
            hasError = true;
            errorMessage = messageSource.getMessage("email.notUnique", null, null);
        }
        if (hasError) {
            model.addAttribute("message", errorMessage);
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("emailPattern", EMAIL);
            return "editEmail";
        }
        // trigger email verification
        try {
            applicationEventPublisher.publishEvent(new OnEmailConfirmationNeededEvent(user, editedEmail));
        }
        catch (Exception me) { //TODO log this situation
            System.out.println("some problem with email sending");
        }
        return "redirect:/emailConfirmationRequest";
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
    public String processEmailConfirmation(@RequestParam("token") String token, Model model, @AuthenticationPrincipal User authUser){
        // error checks
        VerificationToken verificationToken = userServer.getVerificationToken(token);
        boolean hasError = false;
        String errorMessage = null;
        if (verificationToken == null) { // check for invalid token
            hasError = true;
            errorMessage = messageSource.getMessage("auth.invalidToken", null, null);
        }
        else if ((verificationToken.getExpiryDate().getTime() - Calendar.getInstance().getTime().getTime()) <= 0) { // check for expired token
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
        userServer.deleteVerificationToken(verificationToken.getId());
        User user = verificationToken.getUser();
        if (verificationToken.getEditedEmail() == null) { // new user registration process
            userServer.enableUser(user);
            //TODO send welcome email
            return "redirect:/login";
        }
        else { // change email process
            userServer.editEmail(user, verificationToken.getEditedEmail());
            //TODO send email address change confirmation email
            return "redirect:/home";
        }
    }

    @GetMapping("/user/editPassword")
    public String showEditPasswordForm(Model model, @AuthenticationPrincipal User authUser){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("formPassword", new FormPassword());
        model.addAttribute("passwordPattern", PASSWORD);
        return "editPassword";
    }

    @PostMapping("/user/editPassword")
    public String processEditPassword(@Valid FormPassword formPassword,
                                      Errors errors,
                                      Model model,
                                      @AuthenticationPrincipal User authUser) {

        User user = userServer.findById(authUser.getId());
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
        else {
            try{
                userServer.editPassword(authUser.getId(), passwordEncoder, formPassword);
            }
            catch (InvalidPasswordException e){
                hasError = true;
                errors.rejectValue("currentPassword", "password.invalid");
            }
        }
        if (hasError) {
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());;
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("passwordPattern", PASSWORD);
            return "editPassword";
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

}
