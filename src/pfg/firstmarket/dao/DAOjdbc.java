package pfg.firstmarket.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pfg.firstmarket.adt.trees.TreeNode;
import pfg.firstmarket.model.Book;
import pfg.firstmarket.model.CatPath;
import pfg.firstmarket.model.Category;

public class DAOjdbc implements DAO {
	
	private static String connRoute = "jdbc:mysql://localhost:3306/fm?serverTimezone=UTC";
	private static TreeNode<Category> rootCategory;
	private static HashMap<String,String> categoriesMap;
	private static List<CatPath> firstOrderRelations;
	
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			categoriesMap = fetchCategoriesMap("SELECT * FROM categories");
			firstOrderRelations = fetchCatPaths("SELECT * FROM catpaths WHERE path_length=1");
			rootCategory = getRootCategory();
			populate(rootCategory);
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
	
	@Override
	public TreeNode<Category> getCategories() {
		return rootCategory;
	}
	
	private static void populate(TreeNode<Category> node) {
		for (Category c : getChildren(node.getData())) {
			populate(node.addChild(c));
		}
	}
	
	private static List<Category> getChildren(Category c){
		String parent = c.getCategory_Id();
		List<Category> children = new ArrayList<Category>();
		String ancestor = null;
		String descendant = null;
		
		for (CatPath cp : firstOrderRelations) {
			ancestor = cp.getAncestor_id();
			descendant = cp.getDescendant_id();
			if (parent.equals(ancestor)) {
				children.add(new Category(descendant, categoriesMap.get(descendant)));
			}
		}
		return children;
	}
	
	private static TreeNode<Category> getRootCategory() {
		String sql = "SELECT category_id,name FROM (SELECT category_id,name,COUNT(category_id) AS count FROM categories AS c, catpaths AS cp WHERE c.category_id=cp.descendant GROUP BY category_id) AS aux WHERE aux.count=1";
		TreeNode<Category> root = null;
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
			
			System.out.println(rs.getFetchSize());
			root = new TreeNode<Category>(new Category(rs.getString("category_id"),rs.getString("name")));
		}
		catch (SQLException e) { e.printStackTrace(); }
		if (root == null) System.out.println("root null");
		return root;
	}

	private static List<CatPath> fetchCatPaths(String sql) {
		List<CatPath> relations = new ArrayList<CatPath>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
				
			while(rs.next()) {
				CatPath cp = new CatPath(rs.getString("ancestor"),rs.getString("descendant"),rs.getInt("path_length"));
				relations.add(cp);
			}
		}
		catch (SQLException e) { e.printStackTrace(); }
		return relations;
	}
	
	private static HashMap<String,String> fetchCategoriesMap(String sql) {
		HashMap<String,String> categories = new HashMap<String,String>();
		try (Connection connexion = DriverManager.getConnection(connRoute,"root","misrra");
			 Statement stm = connexion.createStatement();
			 ResultSet rs = stm.executeQuery(sql)) {
				
			while(rs.next()) {
				categories.put(rs.getString("category_id"), rs.getString("name"));
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
				Book b = new Book(rs.getString("isbn"),rs.getString("title"));
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
