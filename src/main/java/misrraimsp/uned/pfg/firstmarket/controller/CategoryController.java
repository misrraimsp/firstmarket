package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.CategoryForm;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class CategoryController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private CatServer catServer;
    private BookServer bookServer;

    @Autowired
    public CategoryController(CatServer catServer,
                              BookServer bookServer) {

        this.catServer = catServer;
        this.bookServer = bookServer;
        LOGGER.trace("{} created", this.getClass().getName());
    }

    @GetMapping("/admin/loadCategories")
    public String loadCategories(){
        catServer.loadCategories();
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model) {
        model.addAttribute("jsonStringCategories", catServer.getJSONStringCategories());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        return "categories";
    }

    @GetMapping("/admin/categoryForm")
    public String showCategoryForm(@RequestParam(name = "id") Optional<Long> optCategoryId, Model model){
        AtomicBoolean badIdArgument = new AtomicBoolean(false);
        optCategoryId.ifPresentOrElse(
                categoryId -> {
                    try {
                        Category category = catServer.findCategoryById(categoryId);
                        model.addAttribute("categoryForm", catServer.convertCategoryToCategoryForm(category));
                        populateModelToCategoryForm(model);
                        model.addAttribute("descendants", catServer.getDescendants(categoryId));
                    } catch (IllegalArgumentException e) {
                        badIdArgument.set(true);
                    }
                },
                () -> {
                    model.addAttribute("categoryForm", new CategoryForm());
                    populateModelToCategoryForm(model);
                }
        );
        if (badIdArgument.get()) {
            return "redirect:/admin/categories";
        }
        return "categoryForm";
    }

    @PostMapping("/admin/categoryForm")
    public String processCategoryForm(@Valid CategoryForm categoryForm, Errors errors, Model model){
        boolean isEdition = categoryForm.getCategoryId() != null;
        if (errors.hasErrors()) {
            populateModelToCategoryForm(model);
            if (isEdition){
                try {
                    model.addAttribute("descendants", catServer.getDescendants(categoryForm.getCategoryId()));
                } catch (IllegalArgumentException e) {
                    // TODO log this situation
                    return "redirect:/home";
                }
            }
            return "categoryForm";
        }
        Category category = catServer.convertCategoryFormToCategory(categoryForm);
        if (isEdition){
            catServer.edit(category);
        } else {
            catServer.persist(category);
        }
        return "redirect:/admin/loadCategories";
    }

    private void populateModelToCategoryForm(Model model) {
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("descendants", new ArrayList<>());
    }

    @GetMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        Category deletingCategory = catServer.findCategoryById(id);
        //los libros con la categoría a eliminar pasan a estar vinculados a la categoría del padre
        bookServer.updateCategoryIdByCategoryId(id, deletingCategory.getParent().getId());
        catServer.deleteById(id);
        return "redirect:/admin/loadCategories";
    }

}
