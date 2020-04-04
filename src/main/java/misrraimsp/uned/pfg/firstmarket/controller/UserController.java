package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.event.OnRegistrationCompleteEvent;
import misrraimsp.uned.pfg.firstmarket.exception.EmailAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.exception.InvalidPasswordException;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.VerificationToken;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormPassword;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormUser;
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

        // Check validation errors
        if (errors.hasErrors()) {
            if (errors.hasGlobalErrors()){
                for (ObjectError objectError : errors.getGlobalErrors()){
                    if (objectError.getCode().equals("PasswordMatches")){
                        errors.rejectValue("matchingPassword", "password.notMatching", objectError.getDefaultMessage());
                    }
                    else {
                        // TODO log this state
                        System.out.println(objectError);
                    }
                }
            }
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }

        // Try to persist, catching email non-uniqueness condition
        User registeredUser;
        try {
            registeredUser = userServer.persist(formUser, passwordEncoder, null, null);
        }
        catch (EmailAlreadyExistsException e) {
            errors.rejectValue("email", "email.notUnique");
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }

        // Trigger email verification event
        if (registeredUser != null) {
            try {
                applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(registeredUser));
            }
            catch (Exception me) {
                System.out.println("some problem with email sending");
            }
        }
        else {
            // TODO log this state
            System.out.println("user not registered but no EmailAlreadyExistsException was thrown");
        }

        return "redirect:/newUserSuccess";
    }

    @GetMapping("/newUserSuccess")
    public String showNewUserSuccess(Model model){
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "newUserSuccess";
    }

    @GetMapping("/confirmNewUser")
    public String processConfirmNewUser(@RequestParam("token") String token, Model model){
        VerificationToken verificationToken = userServer.getVerificationToken(token);
        // check for invalid token
        if (verificationToken == null) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("message", messageSource.getMessage("auth.invalidToken", null, null));
            return "newUserError";
        }
        // check for expired token
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("message", messageSource.getMessage("auth.expiredToken", null, null));
            return "newUserError";
        }
        // enable user
        User user = verificationToken.getUser();
        userServer.enable(user);
        return "redirect:/login";
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

    @GetMapping("/user/addPurchase")
    public String processAddPurchase(@AuthenticationPrincipal User authUser){
        userServer.addPurchase(authUser.getId());
        return "redirect:/home";
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
        if (errors.hasErrors()) {
            if (errors.hasGlobalErrors()){
                for (ObjectError objectError : errors.getGlobalErrors()){
                    if (objectError.getCode().equals("PasswordMatches")){
                        errors.rejectValue("matchingPassword", "password.notMatching", objectError.getDefaultMessage());
                    }
                    else{//debug
                        System.out.println(objectError);
                    }
                }
            }
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("passwordPattern", PASSWORD);
            return "editPassword";
        }
        try{
            userServer.editPassword(authUser.getId(), passwordEncoder, formPassword);
        }
        catch (InvalidPasswordException e){
            errors.rejectValue("currentPassword", "password.invalid");
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());;
            model.addAttribute("mainCategories", catServer.getMainCategories());
            model.addAttribute("passwordPattern", PASSWORD);
            return "editPassword";
        }
        return "redirect:/home";
    }

}
