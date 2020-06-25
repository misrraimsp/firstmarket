package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Cart extends BasicEntity {

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Item> items = new HashSet<>();

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Sale> sales = new HashSet<>();

    private String stripePaymentIntentId;

    private String stripeClientSecret;

    public boolean isCommitted() {
        return !sales.isEmpty();
    }

    public int getSize(){
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public BigDecimal getPrice() {
        return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
