package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private String firstName;

    private String lastName;

    private String phone;

    private String email;

    private String streetAddress;

    private String zipCode;

    private String country;

    private String province;
}
