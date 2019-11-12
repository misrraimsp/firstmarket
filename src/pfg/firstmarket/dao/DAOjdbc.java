package pfg.firstmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Book;
import pfg.firstmarket.model.CatPath;
import pfg.firstmarket.model.Category;

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
		return fetchBooks("SELECT * FROM books");
	}

	@Override
	public List<Book> getBooks(List<String> conditions) {
		String sql = "SELECT * FROM books WHERE ";
		for (String condition : conditions) {
			sql += condition + " AND "; 
		}
		sql = sql.substring(0, sql.lastIndexOf(" AND "));//el ultimo ' and ' sobra
		return fetchBooks(sql);
	}

	@Override
	public void insertBook(Book book) {
		
		String sql = "INSERT INTO books (isbn,title,category_id) VALUES (?,?,?)";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		params.add(book.getTitle());
		params.add(book.getCategory().getCategory_id());
		
		update(sql, params);
	}

	@Override
	public void updateBook(Book book) {
		String sql = "UPDATE books SET title=?,category_id=? WHERE isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getTitle());
		params.add(book.getCategory().getCategory_id());
		params.add(book.getIsbn());
		
		update(sql, params);
	}
	
	@Override
	public void deleteBook(Book book) {
		String sql = "DELETE FROM books WHERE isbn=?";
		List<String> params = new ArrayList<String>();
		params.add(book.getIsbn());
		
		update(sql, params);
	}
	
	
	
	@Override
	public Category getRootCategory() {
		String sql = "SELECT category_id,name FROM (SELECT category_id,name,COUNT(category_id) AS count FROM categories AS c, catpaths AS cp WHERE c.category_id=cp.descendant GROUP BY category_id) AS aux WHERE aux.count=1";
		Category root = null;
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
			
			while (rs.next()) {
				root = new Category(rs.getString("category_id"),rs.getString("name"));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return root;
	}
	
	@Override
	public List<Category> getCategories() {
		return fetchCategories("SELECT * FROM categories");
	}
	
	@Override
	public List<CatPath> getCatPaths(List<String> conditions){
		String sql = "SELECT * FROM catpaths WHERE ";
		for (String condition : conditions) {
			sql += condition + " and "; 
		}
		sql = sql.substring(0, sql.lastIndexOf(" and "));//el ultimo ' and ' sobra
		return fetchCatPaths(sql);
	}
	
	@Override
	public void insertCategory(Category category, Category parent_category) {
		String sql = "INSERT INTO categories (name) VALUES (?)";
		List<String> params = new ArrayList<String>();
		params.add(category.getName());
		update(sql, params);
		
		sql = "INSERT INTO catpaths (ancestor,descendant,path_length) SELECT ancestor,(SELECT MAX(category_id) FROM categories),path_length+1 FROM catpaths where descendant=? UNION ALL SELECT (SELECT MAX(category_id) FROM categories),(SELECT MAX(category_id) FROM categories),(SELECT 0)";
		params.clear();
		params.add(parent_category.getCategory_id());
		update(sql, params); 
	}
	
	@Override
	public void updateCategory(Category category, Category parent_category) {
		//update name
		String sql = "UPDATE categories SET name=? WHERE category_id=?";
		List<String> params = new ArrayList<String>();
		params.add(category.getName());
		params.add(category.getCategory_id());
		update(sql, params);
		
		//delete old links
		sql = "DELETE FROM catpaths WHERE descendant IN (SELECT descendant FROM catpaths WHERE ancestor=?) AND ancestor IN (SELECT ancestor FROM catpaths WHERE descendant=? AND ancestor != descendant)";
		params.clear();
		params.add(category.getCategory_id());
		params.add(category.getCategory_id());
		update(sql, params);
		
		//insert new links
		sql = "INSERT INTO catpaths (ancestor, descendant, path_length) SELECT supertree.ancestor, subtree.descendant, supertree.path_length+subtree.path_length+1 FROM catpaths AS supertree CROSS JOIN catpaths AS subtree WHERE supertree.descendant=? AND subtree.ancestor=?;";
		params.clear();
		params.add(parent_category.getCategory_id());
		params.add(category.getCategory_id());
		update(sql, params);
	}
	
	@Override
	public void deleteCategory(Category category, boolean includeSubtree) {
		String category_id = category.getCategory_id();
		String sql = "";
		List<String> params = new ArrayList<String>();
		
		if (includeSubtree) {
			//1- recuperar y guardar en VARIABLE la lista de category_id de las categorias a eliminar
			//   equivalente a: VARIABLE <-- SELECT descendant FROM catpaths WHERE ancestor=?
			List<String> conditions = new ArrayList<String>();
			conditions.add("ancestor=" + category_id);
			String category_id_list = "";
			for (CatPath catpath : getCatPaths(conditions)) {
				category_id_list += catpath.getDescendant() + ",";
			}
			category_id_list = category_id_list.substring(0, category_id_list.lastIndexOf(","));//el ultimo ',' sobra
			
			//2- eliminar todos los caminos que entran o salen de alguna categoria a eliminar
			sql = "DELETE FROM catpaths WHERE (ancestor,descendant) IN (SELECT ancestor,descendant FROM catpaths WHERE ancestor IN (" + category_id_list + ") OR descendant IN (" + category_id_list + "))";
			update(sql, params);
			
			//3- eliminar las categorias en cuestion de la tabla de categorias
			sql = "DELETE FROM categories WHERE category_id IN (" + category_id_list + ")";
			update(sql, params);
		}
		else {
			//update path_length from descendants
			sql = "UPDATE catpaths SET path_length=path_length-1 WHERE (ancestor,descendant) IN (SELECT aux1.ancestor, aux2.descendant FROM (SELECT * FROM catpaths WHERE descendant=? AND ancestor!=?) AS aux1,(SELECT * FROM catpaths WHERE descendant!=? AND ancestor=?) AS aux2)";
			params.add(category_id);
			params.add(category_id);
			params.add(category_id);
			params.add(category_id);
			update(sql, params);
		
			params.clear();
			//delete in and out links
			sql = "DELETE FROM catpaths WHERE (ancestor,descendant) IN (SELECT ancestor,descendant FROM catpaths WHERE ancestor=? OR descendant=?)";
			params.add(category_id);
			params.add(category_id);
			update(sql, params);
		
			params.clear();
			//delete category row
			sql = "DELETE FROM categories WHERE category_id=?";
			params.add(category_id);
			update(sql, params);
		}
	}
	
	@Override
	public void updateBooksOnDeletingCategory(Category deleting_category, Category parent_category, boolean includeSubtree) {
		String deleting_category_id = deleting_category.getCategory_id();
		String parent_category_id = parent_category.getCategory_id();
		String sql = "";
		List<String> params = new ArrayList<String>();
		
		if (includeSubtree) {
			sql = "UPDATE books SET category_id=? WHERE isbn IN (SELECT isbn FROM books WHERE category_id IN (SELECT descendant FROM catpaths WHERE ancestor=?))";
			params.add(parent_category_id);
			params.add(deleting_category_id);
			update(sql, params);
		}
		else {
			sql = "UPDATE books SET category_id=? WHERE isbn IN (SELECT isbn FROM books WHERE category_id=?)";
			params.add(parent_category_id);
			params.add(deleting_category_id);
			update(sql, params);
		}
	}
	
	
	
	private static List<CatPath> fetchCatPaths(String sql) {
		List<CatPath> catpaths = new ArrayList<CatPath>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
				
			while(rs.next()) {
				CatPath cp = new CatPath(rs.getString("ancestor"),rs.getString("descendant"),rs.getInt("path_length"));
				catpaths.add(cp);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return catpaths;
	}
	
	private static List<Category> fetchCategories(String sql) {
		List<Category> categories = new ArrayList<Category>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
				
			while(rs.next()) {
				categories.add(new Category(rs.getString("category_id"), rs.getString("name")));
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return categories;
	}

	private static List<Book> fetchBooks(String sql) {
		List<Book> books = new ArrayList<Book>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
				
			while(rs.next()) {
				Book b = new Book(rs.getString("isbn"),rs.getString("title"),FrontController.cs.getCategory(rs.getString("category_id")));
				books.add(b);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return books;
	}
	
	private static void update(String sql, List<String> params) {
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
