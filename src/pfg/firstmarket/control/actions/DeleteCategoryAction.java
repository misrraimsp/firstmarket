package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Category;

public class DeleteCategoryAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Category c = new Category (request.getParameter("category_id"), null);
		if (request.getParameter("deleteSubtree") == null) {
			FrontController.db.deleteCategory(c);
		}
		else {
			FrontController.db.deleteSubCategories(c);
		}
	}

}
