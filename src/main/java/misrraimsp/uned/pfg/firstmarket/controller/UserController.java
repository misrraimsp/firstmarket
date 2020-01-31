package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
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
        model.addAttribute("user", new User());
        return "newUser";
    }

    @PostMapping("/newUser")
    public String processNewUser(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "newUser";
        }
        userServer.persist(user, passwordEncoder);
        return "redirect:/login";
    }

    @GetMapping("/user/editUser")
    public String showEditUserForm(Model model, @AuthenticationPrincipal User authUser){
        model.addAttribute("title", "Edit Profile");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("user", userServer.findById(authUser.getId()));
        return "editUser";
    }

    @PostMapping("/user/editUser")
    public String processEditUser(@Valid User editedUser,
                                  Errors errors,
                                  Model model,
                                  @AuthenticationPrincipal User authUser) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Profile");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "editUser";
        }
        userServer.edit(editedUser, authUser);
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

}
