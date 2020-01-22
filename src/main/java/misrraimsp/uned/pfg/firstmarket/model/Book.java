package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "enter a valid ISBN code") //TODO: proper isbn server-side validation
    private String isbn;

    private String title;

    @ManyToOne
    private Category category;

    @ManyToOne
    private Image image;

}