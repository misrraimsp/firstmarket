package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class CatPath {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	private Category ancestor;

	@ManyToOne
	private Category descendant;

	private int path_length;

}
