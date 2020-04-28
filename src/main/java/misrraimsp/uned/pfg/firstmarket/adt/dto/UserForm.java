package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.annotation.ValidPattern;


@Data
@PasswordMatches
public class UserForm implements MatchingPassword {

    @ValidPattern(pattern = "email", message = "{email.invalidFormat}")
    private String email;

    @ValidPattern(pattern = "password", message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String firstName;

    @ValidPattern(pattern = "textBasic", message = "{text.basic}")
    private String lastName;
}
