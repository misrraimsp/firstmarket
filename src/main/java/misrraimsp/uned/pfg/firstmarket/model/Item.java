package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Item extends BasicEntity {

    @OneToOne
    private Book book;

    private int quantity;

    public BigDecimal getPrice() {
        return book.getPrice().multiply(new BigDecimal(quantity));
    }
}
