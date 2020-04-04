package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormUser implements MatchingPassword, Constants {

    @Pattern(regexp = EMAIL, message = "{email.invalidFormat}")
    private String email;

    @Pattern(regexp = PASSWORD, message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String firstName;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String lastName;
}
