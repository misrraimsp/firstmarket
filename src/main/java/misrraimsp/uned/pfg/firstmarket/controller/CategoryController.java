package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.CategoryForm;
import misrraimsp.uned.pfg.firstmarket.exception.CategoryNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.NoRootCategoryException;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
        try {
            catServer.loadCategories();
            LOGGER.trace("Categories loaded");
        }
        catch (NoRootCategoryException e) {
            LOGGER.error("Root category not found", e);
            return null;
        }
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model) {
        try {
            model.addAttribute("jsonStringCategories", catServer.getJSONStringCategories());
            model.addAttribute("mainCategories", catServer.getMainCategories());
        }
        catch (NoRootCategoryException e) {
            LOGGER.error("Root category not found", e);
            return null;
        }
        return "categories";
    }

    @GetMapping("/admin/categoryForm")
    public String showCategoryForm(@RequestParam(name = "id") Optional<Long> optCategoryId, Model model){
        AtomicBoolean categoryNotFound = new AtomicBoolean(false);
        optCategoryId.ifPresentOrElse(
                categoryId -> {
                    try {
                        Category category = catServer.findCategoryById(categoryId);
                        model.addAttribute("categoryForm", catServer.convertCategoryToCategoryForm(category));
                        populateModelToCategoryForm(model);
                        model.addAttribute("descendants", catServer.getDescendants(categoryId));
                    } catch (CategoryNotFoundException e) {
                        LOGGER.warn("Trying to edit a non-existent Category", e);
                        categoryNotFound.set(true);
                    }
                },
                () -> {
                    model.addAttribute("categoryForm", new CategoryForm());
                    populateModelToCategoryForm(model);
                }
        );
        if (categoryNotFound.get()) {
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
                } catch (CategoryNotFoundException e) {
                    LOGGER.warn("Trying to edit a non-existent Category", e);
                    return "redirect:/admin/categories";
                }
            }
            return "categoryForm";
        }
        Category category = catServer.convertCategoryFormToCategory(categoryForm);
        if (isEdition){
            catServer.edit(category);
            LOGGER.trace("Category edited (id={})", category.getId());
        } else {
            category = catServer.persist(category);
            LOGGER.trace("Category persisted (id={})", category.getId());
        }
        return "redirect:/admin/loadCategories";
    }

    private void populateModelToCategoryForm(Model model) {
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("mainCategories", catServer.getMainCategories());
        model.addAttribute("descendants", new ArrayList<>());
    }

    @Transactional
    @GetMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        try {
            Category deletingCategory = catServer.findCategoryById(id);
            //los libros con la categoría a eliminar pasan a estar vinculados a la categoría del padre
            bookServer.updateCategoryIdByCategoryId(id, deletingCategory.getParent().getId());
            catServer.deleteCategory(deletingCategory);
            LOGGER.trace("Category deleted (id={})", id);
        }
        catch (CategoryNotFoundException e) {
            LOGGER.warn("Trying to delete a non-existent Category (id={})", id, e);
            return "redirect:/admin/categories";
        }
        return "redirect:/admin/loadCategories";
    }

}
