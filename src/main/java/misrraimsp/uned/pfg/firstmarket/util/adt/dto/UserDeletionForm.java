package misrraimsp.uned.pfg.firstmarket.util.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.DeletionReason;
import misrraimsp.uned.pfg.firstmarket.util.validation.ValidPattern;

@Data
public class UserDeletionForm {

    private String password;

    private DeletionReason deletionReason;

    @ValidPattern(pattern = "textLong", message = "{validation.regex.text-long}")
    private String comment;
}
