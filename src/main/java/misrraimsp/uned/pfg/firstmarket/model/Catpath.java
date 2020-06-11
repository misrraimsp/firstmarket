package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import misrraimsp.uned.pfg.firstmarket.data.Auditable;

import javax.persistence.*;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Catpath extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Category ancestor;

	@ManyToOne
	private Category descendant;

	private int size;

}
