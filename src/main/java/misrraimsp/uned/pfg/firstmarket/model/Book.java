package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Languages;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private List<Author> authors = new ArrayList<>();

    @ManyToOne
    private Publisher publisher;

    private String description;

    private int numPages;

    private Languages language;

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