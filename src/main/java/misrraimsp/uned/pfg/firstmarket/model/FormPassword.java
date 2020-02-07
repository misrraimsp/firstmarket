package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormPassword implements MatchingPassword, Patterns {

    private String currentPassword;

    @Pattern(regexp = PASSWORD_PATTERN,
            message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;
}
