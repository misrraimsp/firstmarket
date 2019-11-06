package pfg.firstmarket.dao;

import java.util.List;

import pfg.firstmarket.adt.TreeNode;
import pfg.firstmarket.model.Book;
import pfg.firstmarket.model.Category;

public interface DAO {

	public List<Book> getBooks();
	public List<Book> getBooks(List<String> conditions);
	public void insertBook(Book book);
	public void updateBook(Book book);
	public void deleteBook(Book book);
	
	public TreeNode<Category> getCategories();
}
