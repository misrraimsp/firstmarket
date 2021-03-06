package misrraimsp.uned.pfg.firstmarket.util.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.util.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidPattern;


@Data
@PasswordMatches
public class PasswordForm implements TwinPasswordContainer {

    private String currentPassword;

    @ValidPattern(pattern = "password", message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;
}
