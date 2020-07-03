package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Address extends BasicEntity {

    private String line1;

    private String line2;

    private String city;

    private String postalCode;

    private String country;

    private String province; //or state
}
