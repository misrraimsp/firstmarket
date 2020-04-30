package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> items = new ArrayList<>();

    //@PastOrPresent(message = "{date.notValid}")
    private LocalDateTime lastModified;

    public int getCartSize(){
        int sum = 0;
        for (Item i : items){
            sum += i.getQuantity();
        }
        return sum;
    }

    public BigDecimal getPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (Item item : items){
            sum = sum.add(item.getPrice());
        }
        return sum;
    }
}
