package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CategoryController {

    private CategoryServer categoryServer;

    @Autowired
    public CategoryController(CategoryServer categoryServer) {
        this.categoryServer = categoryServer;
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model){
        String html = categoryServer.getCategoriesOnHtml();
        model.addAttribute("categoriesOnHtml", html);
        return "categories";
    }

    @GetMapping("/admin/newCategory")
    public String showNewCategoryForm(Model model){
        model.addAttribute("category", new Category());
        model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
        return "newCategory";
    }

    @PostMapping("/admin/newCategory")
    public String processNewCategory(@Valid Category newCategoryInfo, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
            return "newCategory";
        }
        categoryServer.saveNewCategory(newCategoryInfo.getId(), newCategoryInfo.getName());
        categoryServer.loadCategories();
        return "redirect:/admin/categories";
    }

}
