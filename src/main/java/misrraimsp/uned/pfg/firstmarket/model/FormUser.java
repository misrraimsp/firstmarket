package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class FormUser implements MatchingPassword{

    @ValidEmail(message = "{email.notValid}")
    @NotNull
    @NotEmpty(message = "{email.notEmpty}")
    private String email;

    @NotNull
    @NotEmpty(message = "{password.notEmpty}")
    private String password;

    private String matchingPassword;

    private String firstName;

    private String lastName;
}
