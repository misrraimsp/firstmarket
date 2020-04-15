package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.ValidTextBasic;

@Data
public class ProfileForm {

    @ValidTextBasic(message = "{text.basic}")
    private String firstName;

    @ValidTextBasic(message = "{text.basic}")
    private String lastName;
}
