package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;

public class InsertCategoryAction implements Action {
	
	private DAO db;

	public InsertCategoryAction(DAO db) {
		super();
		this.db = db;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		db.insertCategory(request.getParameter("parent_category_id"),request.getParameter("category_name"));
	}

}
