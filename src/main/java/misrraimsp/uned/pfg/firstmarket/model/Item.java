package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Positive;

@Data
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Book book;

    @Positive(message = "{number.positive}")
    private int quantity;
}
