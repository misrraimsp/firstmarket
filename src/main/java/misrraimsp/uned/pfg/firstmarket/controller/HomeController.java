package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private CatServer catServer;
    private BookServer bookServer;

    @Autowired
    public HomeController(CatServer catServer, BookServer bookServer) {
        this.catServer = catServer;
        this.bookServer = bookServer;
    }

    @GetMapping("/")
    public String initialSetUp(Model model){
        catServer.loadCategories();
        model.addAttribute("title", "FirstMarket");
        model.addAttribute("books", bookServer.findAll());
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