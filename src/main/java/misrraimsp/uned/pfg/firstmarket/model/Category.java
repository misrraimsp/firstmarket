package misrraimsp.uned.pfg.firstmarket.model;

import lombok.Data;
import misrraimsp.uned.pfg.firstmarket.config.appParameters.Constants;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Data
@Entity
public class Category implements Constants {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Pattern(regexp = TEXT_BASIC, message = "{text.basic}")
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

	@Override
	public int hashCode() {
		final int PRIME = 59;
		int result = 1;
		final Object $id = this.getId();
		result = result * PRIME + ($id == null ? 43 : $id.hashCode());
		final Object $name = this.getName();
		result = result * PRIME + ($name == null ? 43 : $name.hashCode());
		final Object $parent = this.getParent();
		result = result * PRIME + ($parent == null ? 43 : $parent.hashCode());
		return result;
	}
}
