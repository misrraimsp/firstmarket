package pfg.firstmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
	public List<Book> getAllBooks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Book> getBooksByKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

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
	
	private static PreparedStatement getParameterizedQuery(Connection connexion, String sql, List<String> params) throws SQLException {
		
			PreparedStatement stm=connexion.prepareStatement(sql);
			int index = 1;
			for (String param : params) {
				stm.setString(index, param);
				index++;
			}
			return stm;
	}

}
