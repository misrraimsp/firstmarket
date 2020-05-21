package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Item> items = new HashSet<>();

    private LocalDateTime lastModified;

    private boolean isCommitted;

    private LocalDateTime committedAt;

    private String piId;

    private String piClientSecret;

    public int getSize(){
        return items.stream().mapToInt(Item::getQuantity).sum();
    }

    public BigDecimal getPrice() {
        return items.stream().map(Item::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
