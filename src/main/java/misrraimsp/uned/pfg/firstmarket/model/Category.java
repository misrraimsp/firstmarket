package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;

	@ManyToOne
	private Category parent;

	/**
	 * La igualdad entre categorias se define únicamente a través
	 * de la iguladad de sus Id's
	 *
	 * Esto es así para evitar que una categoría con name indentado
	 * sea diferente a la misma categoría pero sin indentar
	 */
	@Override
	public boolean equals(final Object o) {
		if (o == this) return true;
		if (!(o instanceof Category)) return false;
		final Category other = (Category) o;
		//if (!other.canEqual((Object) this)) return false;
		final Object this$id = this.getId();
		final Object other$id = other.getId();
		if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
		//final Object this$name = this.getName();
		//final Object other$name = other.getName();
		//if (this$name == null ? other$name != null : !this$name.equals(other$name)) return false;
		//final Object this$parent = this.getParent();
		//final Object other$parent = other.getParent();
		//if (this$parent == null ? other$parent != null : !this$parent.equals(other$parent)) return false;
		return true;
	}
}
