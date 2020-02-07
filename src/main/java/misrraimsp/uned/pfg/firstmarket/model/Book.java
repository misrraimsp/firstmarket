package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Book implements Patterns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = ISBN_PATTERN, message = "{isbn.invalidFormat}")
    private String isbn;

    @Pattern(regexp = W_0_30_$, message = "{text.W_0_30_$")
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Image image;

}