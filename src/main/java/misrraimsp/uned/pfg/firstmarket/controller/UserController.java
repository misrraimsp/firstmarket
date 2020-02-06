package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.exception.EmailAlreadyExistsException;
import misrraimsp.uned.pfg.firstmarket.exception.InvalidPasswordException;
import misrraimsp.uned.pfg.firstmarket.model.FormPassword;
import misrraimsp.uned.pfg.firstmarket.model.FormUser;
import misrraimsp.uned.pfg.firstmarket.model.Profile;
import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
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
public class UserController {

    private UserServer userServer;
    private PasswordEncoder passwordEncoder;
    private ImageServer imageServer;

    @Autowired
    public UserController(UserServer userServer, PasswordEncoder passwordEncoder, ImageServer imageServer) {
        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
        this.imageServer = imageServer;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("title", "New User");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("formUser", new FormUser());
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
            model.addAttribute("title", "New User");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "newUser";
        }
        try {
            userServer.persist(formUser, passwordEncoder, null, null);
        }
        catch (EmailAlreadyExistsException e) {
            errors.rejectValue("email", "email.notUnique");
            model.addAttribute("title", "New User");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "newUser";
        }
        return "redirect:/login";
    }

    @GetMapping("/user/editProfile")
    public String showEditUserForm(Model model, @AuthenticationPrincipal User authUser){
        model.addAttribute("title", "Edit Profile");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("profile", userServer.findById(authUser.getId()).getProfile());
        return "editProfile";
    }

    @PostMapping("/user/editProfile")
    public String processEditUser(@Valid Profile profile,
                                  Errors errors,
                                  Model model,
                                  @AuthenticationPrincipal User authUser) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Profile");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "editProfile";
        }
        userServer.editProfile(authUser.getId(), profile);
        return "redirect:/home";
    }

    @GetMapping("/user/cart")
    public String showCart(@AuthenticationPrincipal User authUser, Model model){
        model.addAttribute("title", "Cart");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("items", userServer.findById(authUser.getId()).getCart().getItems());
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
        model.addAttribute("title", "Purchases");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("purchases", userServer.findById(authUser.getId()).getPurchases());
        return "purchases";
    }

    @GetMapping("/user/changePassword")
    public String showChangePassForm(Model model){
        model.addAttribute("title", "Change Password");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("formPassword", new FormPassword());
        return "changePassword";
    }

    @PostMapping("/user/changePassword")
    public String processChangePass(@Valid FormPassword formPassword,
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
            model.addAttribute("title", "Change Password");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "changePassword";
        }
        try{
            userServer.changePassword(authUser.getId(), passwordEncoder, formPassword);
        }
        catch (InvalidPasswordException e){
            errors.rejectValue("currentPassword", "password.invalid");
            model.addAttribute("title", "Change Password");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "changePassword";
        }
        return "redirect:/home";
    }

}
