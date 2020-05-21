package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Book book;

    private int quantity;


    public BigDecimal getPrice() {
        return book.getPrice().multiply(new BigDecimal(quantity));
    }
}
