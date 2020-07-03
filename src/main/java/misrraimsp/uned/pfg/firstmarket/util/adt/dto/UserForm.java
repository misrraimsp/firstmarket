package misrraimsp.uned.pfg.firstmarket.util.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.util.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidPattern;


@Data
@PasswordMatches
public class UserForm implements TwinPasswordContainer {

    @ValidPattern(pattern = "email", message = "{email.invalidFormat}")
    private String email;

    @ValidPattern(pattern = "password", message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

}
