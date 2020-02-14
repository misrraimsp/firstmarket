package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private BookServer bookServer;
    private ImageServer imageServer;
    private CatServer catServer;

    @Autowired
    public HomeController(BookServer bookServer, ImageServer imageServer, CatServer catServer) {
        this.bookServer = bookServer;
        this.imageServer = imageServer;
        this.catServer = catServer;
    }

    @GetMapping({"/", "/home"})
    public String showHome(Model model){
        model.addAttribute("title", "FirstMarket");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("books", bookServer.findAll());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        //return "home";
        return "commonLayout";
    }

    @GetMapping("/login")
    public String showLogin(Model model){
        model.addAttribute("title", "Login");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        return "login";
    }

}