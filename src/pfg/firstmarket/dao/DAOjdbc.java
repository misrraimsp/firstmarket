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
		String sql = "select * from books";
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

	@Override
	public List<Book> getBooks(List<String> conditions) {
		
		//building up the query
		String sql = "select * from books where ";
		for (String condition : conditions) {
			sql += condition + " and "; 
		}
		sql = sql.substring(0, sql.lastIndexOf(" and "));//el ultimo ' and ' sobra
		//building up the return value
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
	
	/**
	@Override
	public List<Book> getBooks(List<String> conditions) {
		
		if (conditions.size() == 0) return getAllBooks();
		System.out.println(conditions.get(0));
		//conditions management
		List<String> paramNames = new ArrayList<String>();
		List<String> paramValues = new ArrayList<String>();
		String[] parts = null;
		for (String condition : conditions) {
			parts = condition.split("=");
			paramNames.add(parts[0]);
			paramValues.add(parts[1]);
		}
		//building up the query
		String sql = "select * from books where ";
		for (String name : paramNames) {
			sql += name + "=?" + " and "; 
		}
		sql = sql.substring(0, sql.lastIndexOf(" and "));//el ultimo ' and ' sobra
		System.out.println(sql);
		//building up the return value
		List<Book> books = new ArrayList<Book>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 PreparedStatement stm = getParameterizedQuery(connexion, sql, paramValues);
			 ResultSet rs = stm.executeQuery(sql)) {
			
			while(rs.next()) {
				Book b = new Book(rs.getString("isbn"),rs.getString("title"));
				books.add(b);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return books;
	}
	*/

	@Override
	public void insertBook(Book book) {
		
		String sql = "insert into books (isbn,title) values (?,?)";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		params.add(book.getTitle());
		
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 PreparedStatement stm = getParameterizedQuery(connexion, sql, params)) {
			
			stm.executeUpdate();
		}
		catch (SQLException e) { e.printStackTrace(); }
	}

	@Override
	public void updateBook(Book book) {
		String sql = "update books set title=? where isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getTitle());
		params.add(book.getIsbn());
		
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 PreparedStatement stm = getParameterizedQuery(connexion, sql, params)) {
			
			stm.executeUpdate();
		}
		catch (SQLException e) { e.printStackTrace(); }
	}
	
	@Override
	public void deleteBook(Book book) {
		String sql = "delete from books where isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		
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
