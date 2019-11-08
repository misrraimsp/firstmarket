package pfg.firstmarket.model;

import java.util.List;

import pfg.firstmarket.control.FrontController;

public class Category {

	private String category_id;
	private String name;
	
	public Category(String category_id, String name) {
		super();
		this.name = name;
		this.category_id = category_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory_id() {
		return category_id;
	}

	public void setCategory_id(String id) {
		this.category_id = id;
	}
	
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
	 */
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
}
