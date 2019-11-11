package pfg.firstmarket.model;

import pfg.firstmarket.control.FrontController;

public class Book {

	private String isbn;
	private String title;
	private Category category;
	//private List<String> author;
	//private String publisher;
	
	public Book(String isbn, String title, Category category) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.category = category;
	}
	
	//Si no se especifica, cada libro queda asociado a la categoría root
	public Book() {
		super();
		this.isbn = null;
		this.title = null;
		this.category = FrontController.cs.getRootCategoryNode().getData();
	}

	//Si no se especifica, cada libro queda asociado a la categoría root
	public Book(String isbn, String title) {
		super();
		this.isbn = isbn;
		this.title = title;
		this.category = FrontController.cs.getRootCategoryNode().getData();
	}

	//Si no se especifica, cada libro queda asociado a la categoría root
	public Book(String isbn) {
		super();
		this.isbn = isbn;
		this.title = null;
		this.category = FrontController.cs.getRootCategoryNode().getData();
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
/*
	public List<String> getAuthor() {
		return author;
	}

	public void setAuthor(List<String> author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	*/
	
}
