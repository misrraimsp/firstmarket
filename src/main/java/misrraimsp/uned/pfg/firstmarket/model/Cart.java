package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cart extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Item> items = new HashSet<>();

    private boolean isCommitted;

    private String stripePaymentIntentId;

    private String stripeClientSecret;

    public int getSize(){
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public BigDecimal getPrice() {
        return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
