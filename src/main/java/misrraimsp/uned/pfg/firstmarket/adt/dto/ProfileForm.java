package misrraimsp.uned.pfg.firstmarket.adt.dto;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Gender;
import misrraimsp.uned.pfg.firstmarket.validation.ValidDate;
import misrraimsp.uned.pfg.firstmarket.validation.ValidPattern;

import java.time.Month;

@Data
@ValidDate
public class ProfileForm implements DateContainer {

    private Long profileId;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String firstName;

    @ValidPattern(pattern = "textBasic", message = "{validation.regex.text-basic}")
    private String lastName;

    //@ValidPattern(pattern = "phone", message = "{validation.regex.phone}")
    private String phone;

    private Gender gender;

    private Integer year;
    private Month month;
    private Integer day;
}
