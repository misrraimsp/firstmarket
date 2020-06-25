package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Catpath extends BasicEntity {

	@ManyToOne
	private Category ancestor;

	@ManyToOne
	private Category descendant;

	private int size;

}
