package pfg.firstmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pfg.firstmarket.model.Book;

public class DAOjdbc implements DAO {
	
	private static String connRoute = "jdbc:mysql://localhost:3306/fm?serverTimezone=UTC";
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			}
		catch (ClassNotFoundException ex) { ex.printStackTrace(); }
	}

	@Override
	public List<Book> getBooks() {
		return fetchBooks("select * from books");
	}

	@Override
	public List<Book> getBooks(List<String> conditions) {
		String sql = "select * from books where ";
		for (String condition : conditions) {
			sql += condition + " and "; 
		}
		sql = sql.substring(0, sql.lastIndexOf(" and "));//el ultimo ' and ' sobra
		return fetchBooks(sql);
	}

	@Override
	public void insertBook(Book book) {
		
		String sql = "insert into books (isbn,title) values (?,?)";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		params.add(book.getTitle());
		
		update(sql, params);
	}

	@Override
	public void updateBook(Book book) {
		String sql = "update books set title=? where isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getTitle());
		params.add(book.getIsbn());
		
		update(sql, params);
	}
	
	@Override
	public void deleteBook(Book book) {
		String sql = "delete from books where isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		
		update(sql, params);
	}
	
	private List<Book> fetchBooks(String sql) {
		List<Book> books = new ArrayList<Book>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
				 Statement stm = connexion.createStatement();
				 ResultSet rs = stm.executeQuery(sql)) {
				
				while(rs.next()) {
					Book b = new Book(rs.getString("isbn"),rs.getString("title"));
					books.add(b);
				}
			}
			catch (SQLException e) { e.printStackTrace(); }
			return books;
	}
	
	private void update(String sql, List<String> params) {
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
				 PreparedStatement stm = getParameterizedQuery(connexion, sql, params)) {
				
				stm.executeUpdate();
			}
			catch (SQLException e) { e.printStackTrace(); }
	}
	
	private static PreparedStatement getParameterizedQuery(Connection connexion, String sql, List<String> params) throws SQLException {
		
		PreparedStatement stm = connexion.prepareStatement(sql);
		int index = 1;
		for (String param : params) {
			stm.setString(index, param);
			index++;
		}
		return stm;
	}

}
