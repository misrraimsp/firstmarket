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

    @NotBlank(message = "isbn cannot be empty")
    private String isbn;

    @NotBlank(message = "title cannot be empty")
    private String title;

    @ManyToOne
    private Category category;

}