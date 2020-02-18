package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private BookServer bookServer;
    private CatServer catServer;

    @Autowired
    public HomeController(BookServer bookServer, CatServer catServer) {
        this.bookServer = bookServer;
        this.catServer = catServer;
    }

    @GetMapping({"/", "/home"})
    public String showHome(Model model){
        model.addAttribute("books", bookServer.findAll());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        return "home";
    }

}