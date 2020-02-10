package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.Patterns;
import misrraimsp.uned.pfg.firstmarket.validation.Isbn;

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
    private String isbnNumbers;

    @Pattern(regexp = W_0_30_$, message = "{text.W_0_30_$")
    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Image image;

    public void setIsbnNumbers(String isbn){
        String isbnNumbers = isbn.replaceAll("X", "10").replaceAll("[^\\d]", "");
        int size = isbnNumbers.length();
        if (size != 10 && size != 11 && size != 13){//cut numbers on isbn head
            isbnNumbers = isbnNumbers.substring(2);
        }
        this.isbnNumbers = isbnNumbers;
    }
}