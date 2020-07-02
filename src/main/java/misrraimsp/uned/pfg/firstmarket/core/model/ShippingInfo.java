package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ShippingInfo extends BasicEntity {

    @ManyToOne
    private Address address;

    private String carrier;

    private String name;

    private String phone;

    private String trackingNumber;
}
