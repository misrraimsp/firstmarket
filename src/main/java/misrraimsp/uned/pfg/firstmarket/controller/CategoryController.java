package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
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

    private CatServer catServer;
    private ImageServer imageServer;

    @Autowired
    public CategoryController(CatServer catServer, ImageServer imageServer) {
        this.catServer = catServer;
        this.imageServer = imageServer;
    }

    @GetMapping("/admin/loadCategories")
    public String loadCategories(){
        catServer.loadCategories();
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model){
        model.addAttribute("title", "Categories");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        return "categories";
    }

    @GetMapping("/admin/newCategory")
    public String showNewCategoryForm(Model model){
        model.addAttribute("title", "New Category");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("category", new Category());
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        return "newCategory";
    }

    @PostMapping("/admin/newCategory")
    public String processNewCategory(@Valid Category category, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            return "newCategory";
        }
        catServer.persist(category);
        return "redirect:/admin/loadCategories";
    }

    @GetMapping("/admin/editCategory/{id}")
    public String showEditCategoryForm(@PathVariable("id") Long id, Model model){
        Category category = catServer.findById(id);
        model.addAttribute("title", "Edit Category");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("category", category);
        model.addAttribute("descendants", catServer.getDescendants(category));
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        return "editCategory";
    }

    @PostMapping("/admin/editCategory")
    public String processEditCategory(@Valid Category category, Errors errors, Model model){
        if (errors.hasErrors()) {
            model.addAttribute("descendants", catServer.getDescendants(category));
            model.addAttribute("indentedCategories", catServer.getIndentedCategories());
            return "editCategory";
        }
        catServer.edit(category);
        return "redirect:/admin/loadCategories";
    }

    @GetMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        catServer.deleteById(id);
        return "redirect:/admin/loadCategories";
    }

}
