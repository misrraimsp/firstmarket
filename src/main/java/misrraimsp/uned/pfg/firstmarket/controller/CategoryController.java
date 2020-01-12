package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.data.CategoryRepository;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.CategoryServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CategoryController {

    private CategoryServer categoryServer;
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryServer categoryServer, CategoryRepository categoryRepository) {
        this.categoryServer = categoryServer;
        this.categoryRepository = categoryRepository;
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
    public String processNewCategory(@Valid Category category, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
            return "newCategory";
        }
        categoryServer.persistCategory(category);
        categoryServer.loadCategories();
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/editCategory/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid category Id: " + id));
        model.addAttribute("category", category);
        model.addAttribute("descendants", categoryServer.getDescendants(category));
        model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
        return "editCategory";
    }

    @PostMapping("/admin/editCategory")
    public String processEditCategory(@Valid Category category, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("descendants", categoryServer.getDescendants(category));
            model.addAttribute("indentedCategories", categoryServer.getIndentedCategories());
            return "editCategory";
        }
        categoryServer.editCategory(category);
        categoryServer.loadCategories();
        return "redirect:/admin/categories";
    }

}
