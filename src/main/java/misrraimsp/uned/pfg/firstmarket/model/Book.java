package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Constants;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Book implements Constants {

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
    @Size(max = MAX_NUM_AUTHORS, message = "{array.overSize}")
    private List<Author> authors = new ArrayList<>();

    @ManyToOne
    private Publisher publisher;

    @Pattern(regexp = TEXT_LONG, message = "{text.long}")
    private String description;

    @Min(value = MIN_NUM_PAGES, message = "{number.range}")
    @Max(value = MAX_NUM_PAGES, message = "{number.range}")
    private int numPages;

    private Language language;

    @Digits(integer = MAX_INTEGER_PRICE, fraction = MAX_FRACTION_PRICE)
    private BigDecimal price;

    @Min(value = MIN_NUM_STOCK, message = "{number.range}")
    @Max(value = MAX_NUM_STOCK, message = "{number.range}")
    private int stock;

    @PastOrPresent(message = "{year.notValid}")
    private Year year;


    public String getAuthorsString(){
        String str = "";
        for (Author author : authors){
            str += " " + author.getLastName() + ", " + author.getFirstName() + ".";
        }
        return str;
    }
}