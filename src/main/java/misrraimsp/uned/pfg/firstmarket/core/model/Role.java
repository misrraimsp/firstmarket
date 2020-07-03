package misrraimsp.uned.pfg.firstmarket.core.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Role extends BasicEntity {

    private String name;

}
