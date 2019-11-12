package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Category;

public class DeleteCategoryAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Category deleting_category = new Category (request.getParameter("category_id"), null);//categoria a eliminar
		Category parent_category = FrontController.cs.getParent(deleting_category);//ancestro inmediato de la categoria a eliminar
		boolean includeSubtree = request.getParameter("deleteSubtree") != null;
		FrontController.db.updateBooksOnDeletingCategory(deleting_category, parent_category, includeSubtree);
		FrontController.db.deleteCategory(deleting_category, includeSubtree);
	}

}
