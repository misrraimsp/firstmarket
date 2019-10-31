package pfg.firstmarket.model;

//import java.util.List;

public class Book {

	private String isbn;
	private String title;
	//private List<String> author;
	//private String category;
	//private String publisher;
	
	public Book() {
		super();
	}
	
	

	public Book(String isbn, String title) {
		super();
		this.isbn = isbn;
		this.title = title;
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
/*
	public List<String> getAuthor() {
		return author;
	}

	public void setAuthor(List<String> author) {
		this.author = author;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	*/
	
}
