package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private CategoryServer categoryServer;

    @Autowired
    public HomeController(CategoryServer categoryServer) {
        this.categoryServer = categoryServer;
    }

    @GetMapping("/")
    public String initialSetUp(Model model){
        categoryServer.loadCategories();
        model.addAttribute("title", "FirstMarket");
        return "home";
    }

    @GetMapping("/admin")
    public String showAdmin(Model model){
        model.addAttribute("title", "Admin");
        return "admin";
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        model.addAttribute("title", "Login");
        return "login";
    }

}