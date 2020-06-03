package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="Pedido")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER)
    private Set<Item> items = new HashSet<>();

    @OneToOne
    private ShippingInfo shippingInfo;

    @OneToOne
    private Payment payment;

    private String date;
}
