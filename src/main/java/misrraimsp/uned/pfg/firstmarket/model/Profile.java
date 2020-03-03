package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Pattern;

@Entity
@Data
public class Profile implements Constants {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String firstName;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String lastName;
}
