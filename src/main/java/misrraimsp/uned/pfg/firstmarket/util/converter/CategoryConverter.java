package misrraimsp.uned.pfg.firstmarket.util.converter;

import misrraimsp.uned.pfg.firstmarket.core.model.Category;
import misrraimsp.uned.pfg.firstmarket.util.adt.dto.CategoryForm;
import org.springframework.stereotype.Component;

@Component
public class CategoryConverter {

    public CategoryForm convertCategoryToCategoryForm(Category category) {
        CategoryForm categoryForm = new CategoryForm();
        categoryForm.setCategoryId(category.getId());
        categoryForm.setName(category.getName());
        categoryForm.setParent(category.getParent());
        return categoryForm;
    }

    public Category convertCategoryFormToCategory(CategoryForm categoryForm) {
        Category category = new Category();
        category.setId(categoryForm.getCategoryId());
        category.setName(this.convertCategoryFormName(categoryForm.getName()));
        category.setParent(categoryForm.getParent());
        return category;
    }

    private String convertCategoryFormName(String name) {
        return (!name.isBlank()) ? name : "UNKNOWN-NAME";
    }
}
