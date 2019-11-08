package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Category;

public class UpdateCategoryAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FrontController.db.updateCategory(new Category(request.getParameter("category_id"), request.getParameter("category_name")), new Category(request.getParameter("parent_category_id"),null));
	}

}
