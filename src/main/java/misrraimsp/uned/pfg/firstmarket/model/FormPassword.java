package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormPassword implements MatchingPassword, Constants {

    private String currentPassword;

    @Pattern(regexp = PASSWORD, message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;
}
