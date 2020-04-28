package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPattern;

@Data
public class ProfileForm {

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String firstName;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String lastName;
}
