package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.CategoryForm;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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

@Controller
public class CategoryController extends BasicController {

    @Autowired
    public CategoryController(UserServer userServer,
                              BookServer bookServer,
                              CatServer catServer,
                              ImageServer imageServer,
                              MessageSource messageSource,
                              OrderServer orderServer) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
    }

    private void populateModelToCategoryForm(Model model) {
        model.addAttribute("indentedCategories", catServer.getIndentedCategories());
        model.addAttribute("descendants", new ArrayList<>());
    }

    @GetMapping("/admin/loadCategories")
    public String loadCategories(){
        catServer.loadCategories();
        LOGGER.debug("Categories loaded");
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories")
    public String showCategories(Model model) {
        populateModel(model, null);
        model.addAttribute("jsonStringCategories", catServer.getJSONStringCategories());
        return "categories";
    }

    @GetMapping("/admin/categoryForm")
    public String showCategoryForm(@RequestParam(name = "id") Optional<Long> optCategoryId,
                                   Model model) {

        optCategoryId.ifPresentOrElse(
                categoryId -> {
                    Category category = catServer.findCategoryById(categoryId);
                    model.addAttribute("categoryForm", catServer.convertCategoryToCategoryForm(category));
                    populateModelToCategoryForm(model);
                    model.addAttribute("descendants", catServer.getDescendants(categoryId));
                },
                () -> {
                    model.addAttribute("categoryForm", new CategoryForm());
                    populateModelToCategoryForm(model);
                }
        );
        populateModel(model, null);
        return "categoryForm";
    }

    @PostMapping("/admin/categoryForm")
    public String processCategoryForm(@Valid CategoryForm categoryForm,
                                      Errors errors,
                                      Model model) {

        boolean isEdition = categoryForm.getCategoryId() != null;
        if (errors.hasErrors()) {
            populateModel(model, null);
            populateModelToCategoryForm(model);
            if (isEdition){
                model.addAttribute("descendants", catServer.getDescendants(categoryForm.getCategoryId()));
            }
            return "categoryForm";
        }
        Category category = catServer.convertCategoryFormToCategory(categoryForm);
        if (isEdition){
            catServer.edit(category);
            LOGGER.debug("Category edited (id={})", category.getId());
        } else {
            category = catServer.persist(category);
            LOGGER.debug("Category persisted (id={})", category.getId());
        }
        return "redirect:/admin/loadCategories";
    }

    @Transactional
    @GetMapping("/admin/deleteCategory/{id}")
    public String deleteCategory(@PathVariable("id") Long id){
        Category deletingCategory = catServer.findCategoryById(id);
        //los libros con la categoría a eliminar pasan a estar vinculados a la categoría del padre
        bookServer.updateCategoryIdByCategoryId(id, deletingCategory.getParent().getId());
        catServer.deleteCategory(deletingCategory);
        LOGGER.debug("Category deleted (id={})", id);
        return "redirect:/admin/loadCategories";
    }

}
