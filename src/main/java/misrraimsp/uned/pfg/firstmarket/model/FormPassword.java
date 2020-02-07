package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;

import javax.validation.constraints.Pattern;

@Data
@PasswordMatches
public class FormPassword implements MatchingPassword{

    private String currentPassword;

    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$",
            message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;
}
