package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String line1;

    private String line2;

    private String city;

    private String postalCode;

    private String country;

    private String province; //or state
}
