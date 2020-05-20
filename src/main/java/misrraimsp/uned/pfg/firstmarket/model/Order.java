package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name="PEDIDO")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany
    private Set<Item> items = new HashSet<>();

    @OneToOne
    private ShippingInfo shippingInfo;

    @OneToOne
    private Payment payment;

    private LocalDateTime createdAt;
}
