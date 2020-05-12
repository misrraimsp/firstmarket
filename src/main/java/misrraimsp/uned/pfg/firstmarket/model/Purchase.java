package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Purchase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany
    private Set<Item> items = new HashSet<>();

    @ManyToOne
    private User user;

    @ManyToOne
    private Address address;//restringido a ser una direccion del usuario

    @OneToOne
    private Payment payment;

    private LocalDateTime created;
}
