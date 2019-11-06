package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.adt.TreeNode;
import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.model.Category;

public class InitialSetupAction implements Action {

	private DAO db;

	public InitialSetupAction(DAO db) {
		super();
		this.db = db;
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		TreeNode<Category> categoryCatalog = db.getCategories();
		request.getServletContext().setAttribute("categoryCatalog", categoryCatalog);
	}

}
