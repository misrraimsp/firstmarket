package misrraimsp.uned.pfg.firstmarket.model;

//import java.util.List;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank(message = "name cannot be empty")
	private String name;


	/*
	public Category getParent(){
		return FrontController.cs.getParent(this);
	}
	
	public List<Category> getDescendants(){
		return FrontController.cs.getDescendants(this);
	}

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
