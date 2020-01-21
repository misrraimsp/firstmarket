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
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class UserController {

    private UserServer userServer;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserServer userServer, PasswordEncoder passwordEncoder) {
        this.userServer = userServer;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("title", "User Registration");
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping
    public String processRegistration(@Valid User user, Errors errors) {
        if (errors.hasErrors()) {
            return "registration";
        }
        userServer.persistUser(user, passwordEncoder);
        return "redirect:/login";
    }
}