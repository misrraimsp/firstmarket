package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class FormPassword implements MatchingPassword{

    @NotNull
    @NotEmpty(message = "{password.notEmpty}")
    private String currentPassword;

    @NotNull
    @NotEmpty(message = "{password.notEmpty}")
    private String password;

    private String matchingPassword;
}
