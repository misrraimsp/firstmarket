package misrraimsp.uned.pfg.firstmarket.util.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.core.model.Category;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidPattern;

@Data
public class CategoryForm {

    private Long categoryId;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String name;

    private Category parent;
}
