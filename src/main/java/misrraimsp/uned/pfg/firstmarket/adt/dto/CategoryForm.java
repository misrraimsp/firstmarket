package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.model.Category;
import misrraimsp.uned.pfg.firstmarket.validation.ValidTextBasic;

@Data
public class CategoryForm {

    private Long categoryId;

    @ValidTextBasic(message = "{text.basic}")
    private String name;

    private Category parent;
}
