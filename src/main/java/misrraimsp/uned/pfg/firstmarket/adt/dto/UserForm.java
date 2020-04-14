package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidEmail;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPassword;
import misrraimsp.uned.pfg.firstmarket.validation.ValidTextBasic;


@Data
@PasswordMatches
public class UserForm implements MatchingPassword {

    @ValidEmail(message = "{email.invalidFormat}")
    private String email;

    @ValidPassword(message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

    @ValidTextBasic(message = "{text.basic}")
    private String firstName;

    @ValidTextBasic(message = "{text.basic}")
    private String lastName;
}
