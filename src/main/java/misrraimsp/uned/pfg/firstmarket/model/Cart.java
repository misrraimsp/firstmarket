package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.PastOrPresent;
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

    @PastOrPresent(message = "{date.notValid}")
    private LocalDateTime lastModified;
}
