package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.adt.trees.TreeNode;
import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;
import pfg.firstmarket.model.Category;

public class GetCategoriesAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO db = new DAOjdbc();
		TreeNode<Category> categoryCatalog = db.getCategories();
		
		
		for (TreeNode<Category> node : categoryCatalog) {
			String indent = createIndent(node.getLevel());
			System.out.println(indent + node.getData().getCategory_Id() + "-" + node.getData().getName());
		}
		
		
		request.setAttribute("categoryCatalog", categoryCatalog);
	}

	private static String createIndent(int depth) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < depth; i++) {
			sb.append(' ');
		}
		return sb.toString();
	}
	
}




