package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.User;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.UserServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private BookServer bookServer;
    private CatServer catServer;
    private UserServer userServer;

    @Autowired
    public HomeController(BookServer bookServer, CatServer catServer, UserServer userServer) {
        this.bookServer = bookServer;
        this.catServer = catServer;
        this.userServer = userServer;
    }

    @GetMapping({"/", "/home"})
    public String showHome(Model model, @AuthenticationPrincipal User authUser){
        if (authUser != null){
            User user = userServer.findById(authUser.getId());
            model.addAttribute("firstName", user.getProfile().getFirstName());
            model.addAttribute("cartSize", user.getCart().getCartSize());
        }
        model.addAttribute("books", bookServer.findAll());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "home";
    }

}