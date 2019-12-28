package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

@Data
//@RequiredArgsConstructor
//@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
public class Book {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String isbn;
    private String title;

}