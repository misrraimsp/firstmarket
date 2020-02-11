package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;
import misrraimsp.uned.pfg.firstmarket.validation.ValidImage;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Book implements Patterns {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Isbn(message = "{isbn.error}")
    private String isbn;

    @Pattern(regexp = W_0_30_$, message = "{text.W_0_30_$")
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    @ValidImage(message = "{image.error}")
    private Image image;

    public void setIsbn(String isbn){
        String isbnNumbers = isbn.replaceAll("X", "10").replaceAll("[^\\d]", "");
        int size = isbnNumbers.length();
        if (size != 10 && size != 11 && size != 13){//cut numbers on isbn head
            isbnNumbers = isbnNumbers.substring(2);
            size -= 2;
        }
        if (size == 11){//substitute ...10 by ...X
            isbnNumbers = isbnNumbers.substring(0,9);
            isbnNumbers += "X";
        }
        this.isbn = isbnNumbers;
    }
}