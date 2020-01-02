package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CategoryController {

    private CategoryServer categoryServer;

    @Autowired
    public CategoryController(CategoryServer categoryServer) {
        this.categoryServer = categoryServer;
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model){
        model.addAttribute("rootNode", categoryServer.getRootCategoryNode());
        return "categories";
    }
}
