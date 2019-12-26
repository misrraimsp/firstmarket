package misrraimsp.uned.pfg.firstmarket.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access= AccessLevel.PRIVATE, force=true)
@Entity
public class Book {

    @Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private final Long id;

    private final String isbn;
    private final String title;
    //private Category category;
}