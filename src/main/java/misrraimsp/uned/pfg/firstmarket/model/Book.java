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

    @Pattern(regexp = ISBN_PATTERN,
            message = "{isbn.invalidFormat}")
    private String isbn;

    @Pattern(regexp = TEXT_BASIC_PATTERN,
            message = "{text.basic.invalidFormat}")
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Image image;

}