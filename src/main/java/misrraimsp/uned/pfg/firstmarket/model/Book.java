package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.Language;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String isbn;

    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Image image;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "books_authors",
            joinColumns = @JoinColumn(name = "book_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "author_id", referencedColumnName = "id"))
    private Set<Author> authors = new HashSet<>();

    @ManyToOne
    private Publisher publisher;

    private String description;

    private int pages;

    private Language language;

    private BigDecimal price;

    private int stock;

    private int year;


    public String getAuthorsString(){
        String str = "";
        for (Author author : authors){
            str += " " + author.getLastName() + ", " + author.getFirstName() + ".";
        }
        return str;
    }
}