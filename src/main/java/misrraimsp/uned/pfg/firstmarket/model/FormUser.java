package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidEmail;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@PasswordMatches
public class FormUser {

    @ValidEmail
    @NotNull
    @NotEmpty(message = "email cannot be empty")
    private String email;

    @NotNull
    @NotEmpty(message = "password cannot be empty")
    private String password;
    private String matchingPassword;

    @NotNull
    @NotEmpty(message = "first name cannot be empty")
    private String firstName;

    @NotNull
    @NotEmpty(message = "last name cannot be empty")
    private String lastName;
}
