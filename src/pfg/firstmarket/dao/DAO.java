package pfg.firstmarket.dao;

import java.util.List;

import pfg.firstmarket.model.Book;

public interface DAO {

	public List<Book> getAllBooks();
	public List<Book> getBooksByKey(String key);
	public void insertBook(Book book);
}
