package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.OrderStatus;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name="Pedido")
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Item> items = new HashSet<>();

    @OneToOne
    private ShippingInfo shippingInfo;

    @OneToOne
    private Payment payment;

}
