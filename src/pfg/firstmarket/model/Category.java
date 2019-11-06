package pfg.firstmarket.model;

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
	
	
}
