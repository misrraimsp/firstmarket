package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/* La igualdad entre Category's se define únicamente a través de la igualdad de sus Id's.
   Esto evita que una categoría con name indentado sea diferente a la misma categoría pero sin indentar */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Entity
public class Category extends BasicEntity {

	private String name;

	@ManyToOne
	private Category parent;

	public boolean isRoot() {
		return id.equals(parent.id);
	}

}
