package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public UserController(UserServer userServer, PasswordEncoder passwordEncoder) {
        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/newUser")
    public String showNewUserForm(Model model) {
        model.addAttribute("title", "New User");
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
}
