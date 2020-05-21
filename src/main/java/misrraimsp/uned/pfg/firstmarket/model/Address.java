package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String line1;

    private String line2;

    private String city;

    private String postalCode;

    private String country;

    private String province; //or state
}
