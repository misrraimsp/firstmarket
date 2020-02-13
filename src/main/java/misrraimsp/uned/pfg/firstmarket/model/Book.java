package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Book implements Patterns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Isbn(message = "{isbn.error}")
    private String isbn;

    @Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    @ValidImage(message = "{image.error}")
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private List<Author> authors = new ArrayList<>();

    @ManyToOne
    private Publisher publisher;

    @Pattern(regexp = TEXT_LONG, message = "{text.long}")
    private String summary;

    @Min(value = 1, message = "{number.outOfRange}")
    @Max(value = 5000, message = "{number.outOfRange}")
    private int numPages;

    private Language language;

    @Min(value = 0, message = "{number.outOfRange}")
    @Max(value = 1000000, message = "{number.outOfRange}")
    private double price;

    @Min(value = 0, message = "{number.outOfRange}")
    @Max(value = 100000000, message = "{number.outOfRange}")
    private int stock;

}