package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ShippingInfo extends Auditable {

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
