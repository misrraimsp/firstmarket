package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class FormPassword implements MatchingPassword{

    @NotNull
    @NotEmpty(message = "current password cannot be empty")
    private String currentPassword;

    @NotNull
    @NotEmpty(message = "new password cannot be empty")
    private String password;

    @NotNull
    @NotEmpty(message = "confirm password cannot be empty")
    private String matchingPassword;
}
