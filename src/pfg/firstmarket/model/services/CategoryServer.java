package pfg.firstmarket.model.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pfg.firstmarket.adt.TreeNode;
import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.model.CatPath;
import pfg.firstmarket.model.Category;

public class CategoryServer {
	
	private static TreeNode<Category> rootCategoryNode;
	
	private DAO db;

	public CategoryServer(DAO db) {
		super();
		this.db = db;
	}


	public void loadCategories() {
		rootCategoryNode = new TreeNode<Category>(db.getRootCategory());
		populate(rootCategoryNode, getCategoriesMap(), getFirstOrderPaths());
	}

	public TreeNode<Category> getRootCategoryNode() {
		return rootCategoryNode;
	}
	
	
	
	private static void populate(TreeNode<Category> node, HashMap<String,String> categoriesMap, List<CatPath> firstOrderRelations) {
		for (Category c : getChildren(node.getData(), categoriesMap, firstOrderRelations)) {
			populate(node.addChild(c), categoriesMap, firstOrderRelations);
		}
	}
	
	private static List<Category> getChildren(Category c, HashMap<String,String> categoriesMap, List<CatPath> firstOrderRelations){
		String parent = c.getCategory_id();
		List<Category> children = new ArrayList<Category>();
		String ancestor = null;
		String descendant = null;
		
		for (CatPath cp : firstOrderRelations) {
			ancestor = cp.getAncestor();
			descendant = cp.getDescendant();
			if (parent.equals(ancestor)) {
				children.add(new Category(descendant, categoriesMap.get(descendant)));
			}
		}
		return children;
	}
	
	private HashMap<String,String> getCategoriesMap() {
		List<Category> list = db.getCategories();
		HashMap<String,String> categoriesMap = new HashMap<String,String>();
		for (Category c : list) {
			categoriesMap.put(c.getCategory_id(), c.getName());
		}
		return categoriesMap;
	}
	
	private List<CatPath> getFirstOrderPaths() {
		List<String> conditions = new ArrayList<String>();
		conditions.add("path_length=1");
		return db.getCatPaths(conditions);
	}
}
