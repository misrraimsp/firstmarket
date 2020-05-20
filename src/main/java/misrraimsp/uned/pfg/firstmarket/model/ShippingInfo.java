package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class ShippingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Address address;

    private String carrier;

    private String name;

    private String phone;

    private String trackingNumber;
}
