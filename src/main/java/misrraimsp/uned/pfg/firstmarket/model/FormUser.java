package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidEmail;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormUser implements MatchingPassword{

    @ValidEmail(message = "{email.invalidFormat}")
    private String email;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;

    private String firstName;

    private String lastName;
}
