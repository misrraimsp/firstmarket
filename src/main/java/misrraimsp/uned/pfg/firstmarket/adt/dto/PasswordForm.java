package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.validation.PasswordMatches;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPassword;


@Data
@PasswordMatches
public class PasswordForm implements MatchingPassword {

    private String currentPassword;

    @ValidPassword(message = "{password.invalidFormat}")
    private String password;
    private String matchingPassword;
}