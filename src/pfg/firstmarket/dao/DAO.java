package pfg.firstmarket.dao;

import java.util.List;

import pfg.firstmarket.model.Book;

public interface DAO {

	public List<Book> getAllBooks();
	public List<Book> getBooksByKey(List<String> conditions);
	public void insertBook(Book book);
	public void updateBook(Book book);
}
