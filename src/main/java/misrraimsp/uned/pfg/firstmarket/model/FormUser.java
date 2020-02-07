package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidEmail;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormUser implements MatchingPassword, Patterns {

    @ValidEmail(message = "{email.invalidFormat}")
    private String email;

    @Pattern(regexp = PASSWORD_PATTERN, message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

    @Pattern(regexp = W_0_30_$, message = "{text.W_0_30_$")
    private String firstName;

    @Pattern(regexp = W_0_30_$, message = "{text.W_0_30_$")
    private String lastName;
}
