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
    public String showEditUserForm(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("title", "Edit Profile");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("user", user);
        return "editUser";
    }

    @PostMapping("/user/editUser")
    public String processEditUser(@Valid User editedUser,
                                  Errors errors,
                                  Model model,
                                  @AuthenticationPrincipal User currentUser) {
        if (errors.hasErrors()) {
            model.addAttribute("title", "Edit Profile");
            model.addAttribute("logoId", imageServer.getDefaultImageId());
            return "editUser";
        }
        userServer.edit(editedUser, currentUser);
        return "redirect:/home";
    }
}
