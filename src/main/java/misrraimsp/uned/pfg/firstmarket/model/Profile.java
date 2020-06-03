package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Gender;

import javax.persistence.*;

@Entity
@Data
public class Profile {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String firstName;

    private String lastName;

    private String phone;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String birthDate;

}
