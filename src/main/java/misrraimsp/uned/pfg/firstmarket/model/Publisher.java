package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Publisher extends BasicEntity {

    private String name;

    @Transient
    private int numOfBooks;
}
