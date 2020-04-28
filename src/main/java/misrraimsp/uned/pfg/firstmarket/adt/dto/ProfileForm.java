package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidPattern;

@Data
public class ProfileForm {

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String firstName;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String lastName;
}
