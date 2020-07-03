package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Item extends BasicEntity {

    @ManyToOne
    private Book book;

    private int quantity;

    public BigDecimal getPrice() {
        return book.getPrice().multiply(new BigDecimal(quantity));
    }

}
