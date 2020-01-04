package misrraimsp.uned.pfg.firstmarket.model;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "name cannot be empty")
	private String name;

	@ManyToOne
	private Category parent;

	public boolean isRootCategory(){
		return parent == null;
	}

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

	/*

	/**
	 * condition is of the form of a SQL condition: param='value'
	 * @param condition
	 * @return whether condition is satisfied or not
	 *
	public boolean satisfy(String condition) {
		String[] parts = condition.split("=");
		String propertyName = parts[0];
		String propertyValue = parts[1].substring(1, parts[1].lastIndexOf("'"));//getting rid of the single quote marks around value
		
		switch (propertyName) {
		case "category_id":
			return propertyValue.equals(category_id);
		case "name":
			return propertyValue.equals(name);
		default:
			return false;
		}
	}
	*/
}
