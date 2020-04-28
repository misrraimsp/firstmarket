package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidPattern;

@Data
public class CategoryForm {

    private Long categoryId;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String name;

    private Category parent;
}
