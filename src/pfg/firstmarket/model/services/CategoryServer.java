package pfg.firstmarket.model.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pfg.firstmarket.adt.TreeNode;
import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.CatPath;
import pfg.firstmarket.model.Category;

public class CategoryServer {
	
	private TreeNode<Category> rootCategoryNode;

	public CategoryServer() {
		super();
	}

	public void loadCategories() {
		rootCategoryNode = new TreeNode<Category>(FrontController.db.getRootCategory());
		populate(rootCategoryNode, getCategoriesMap(), getFirstOrderPaths());
	}

	public TreeNode<Category> getRootCategoryNode() {
		return rootCategoryNode;
	}
	
	public List<Category> getCategories(List<String> sqlConditions) {
		List<Category> list = new ArrayList<Category>();
		for (TreeNode<Category> node : rootCategoryNode) {
			Category category = node.getData();
			for (String condition : sqlConditions) {
				if (category.satisfy(condition)) list.add(category);
			}
		}
		return list;
	}
	
	public List<Category> getCategories(){
		List<Category> list = new ArrayList<Category>();
		for (TreeNode<Category> node : rootCategoryNode) {
			list.add(node.getData());
		}
		return list;
	}
	
	public List<Category> getIndentedCategories(){
		List<Category> list = new ArrayList<Category>();
		for (TreeNode<Category> node : rootCategoryNode) {
			String indent = createIndent(node.getLevel());
			Category indentedCategory = new Category(node.getData().getCategory_id(),indent + node.getData().getName());
			list.add(indentedCategory);
		}
		return list;
	}
	
	public Category getParent(Category childCategory) {
		for (TreeNode<Category> parent : rootCategoryNode) {
			for (TreeNode<Category> child : parent.getChildren()) {
				if (child.getData().getCategory_id().equals(childCategory.getCategory_id())) {
					return parent.getData();
				}
			}
		}
		return null;
	}
	
	public List<Category> getDescendants(Category category) {
		List<Category> list = new ArrayList<Category>();
		TreeNode<Category> subtreeRoot = null;
		for (TreeNode<Category> node : rootCategoryNode) {
			if (node.getData().getCategory_id().equals(category.getCategory_id())) {
				subtreeRoot = node;
				break;
			}
		}
		if (subtreeRoot == null) return null;
		for (TreeNode<Category> node : subtreeRoot) {
			list.add(node.getData());
		}
		for (Category c : list) {
			System.out.println(c.getName());
		}
		return list;
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
		List<Category> list = FrontController.db.getCategories();
		HashMap<String,String> categoriesMap = new HashMap<String,String>();
		for (Category c : list) {
			categoriesMap.put(c.getCategory_id(), c.getName());
		}
		return categoriesMap;
	}
	
	private List<CatPath> getFirstOrderPaths() {
		List<String> conditions = new ArrayList<String>();
		conditions.add("path_length=1");
		return FrontController.db.getCatPaths(conditions);
	}
	
	private String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append('-');
		}
		return sb.toString();
	}

}
