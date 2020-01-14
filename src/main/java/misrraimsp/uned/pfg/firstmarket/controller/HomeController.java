package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private CategoryServer categoryServer;

    @Autowired
    public HomeController(CategoryServer categoryServer) {
        this.categoryServer = categoryServer;
    }

    @GetMapping("/")
    public String initialSetUp(){
        categoryServer.loadCategories();
        return "home";
    }

}