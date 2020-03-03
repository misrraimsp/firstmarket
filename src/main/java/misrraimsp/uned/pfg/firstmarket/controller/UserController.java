package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.exception.EmailAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.exception.InvalidPasswordException;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormPassword;
import misrraimsp.uned.pfg.firstmarket.model.dto.FormUser;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class UserController implements Constants {

    private UserServer userServer;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServer userServer, PasswordEncoder passwordEncoder) {
        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("formUser", new FormUser());
        model.addAttribute("emailPattern", EMAIL);
        model.addAttribute("textBasicPattern", TEXT_BASIC);
        model.addAttribute("passwordPattern", PASSWORD);
        return "newUser";
    }

    @PostMapping("/newUser")
    public String processNewUser(@Valid FormUser formUser,
                                 Errors errors,
                                 Model model) {
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
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }
        try {
            userServer.persist(formUser, passwordEncoder, null, null);
        }
        catch (EmailAlreadyExistsException e) {
            errors.rejectValue("email", "email.notUnique");
            model.addAttribute("emailPattern", EMAIL);
            model.addAttribute("textBasicPattern", TEXT_BASIC);
            model.addAttribute("passwordPattern", PASSWORD);
            return "newUser";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/editProfile")
    public String showEditProfileForm(Model model, @AuthenticationPrincipal User authUser){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
        model.addAttribute("profile", user.getProfile());
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
        return "cart";
    }

    @GetMapping("/user/addBook/{id}")
    public String processAddBook(@AuthenticationPrincipal User authUser, @PathVariable("id") Long bookId){
        userServer.addBookToCart(authUser.getId(), bookId);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/removeBook/{id}")
    public String processRemoveBook(@AuthenticationPrincipal User authUser, @PathVariable("id") Long bookId){
        userServer.removeBookFromCart(authUser.getId(), bookId);
        return "redirect:/user/cart";
    }

    @GetMapping("/user/removeItem/{id}")
    public String processRemoveItem(@AuthenticationPrincipal User authUser, @PathVariable("id") Long itemId){
        userServer.removeItemFromCart(authUser.getId(), itemId);
        return "redirect:/user/cart";
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
        return "purchases";
    }

    @GetMapping("/user/editPassword")
    public String showEditPasswordForm(Model model, @AuthenticationPrincipal User authUser){
        User user = userServer.findById(authUser.getId());
        model.addAttribute("firstName", user.getProfile().getFirstName());
        model.addAttribute("cartSize", user.getCart().getCartSize());
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
            model.addAttribute("passwordPattern", PASSWORD);
            return "editPassword";
        }
        return "redirect:/home";
    }

}
