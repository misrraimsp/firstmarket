package pfg.firstmarket.model;

public class Category {

	private String name;
	private String category_id;
	
	public Category(String name, String id) {
		super();
		this.name = name;
		this.category_id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory_Id() {
		return category_id;
	}

	public void setCategory_Id(String id) {
		this.category_id = id;
	}
	
	
}
