package pfg.firstmarket.dao;

import java.util.List;

import pfg.firstmarket.model.Book;
import pfg.firstmarket.model.CatPath;
import pfg.firstmarket.model.Category;

public interface DAO {

	public List<Book> getBooks();
	public List<Book> getBooks(List<String> conditions);
	public void insertBook(Book book);
	public void updateBook(Book book);
	public void deleteBook(Book book);
	
	public List<Category> getCategories();
	public Category getRootCategory();
	public List<CatPath> getCatPaths(List<String> conditions);
	public void insertCategory(Category category, Category parent_category);
	public void updateCategory(Category category, Category parent_category);
	public void deleteCategory(Category c);
	public void deleteSubCategories(Category c);
	
}
