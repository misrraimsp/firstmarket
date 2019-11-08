package pfg.firstmarket.control.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Category;

public class GetCategoriesAction extends GetAction implements Action {

	public GetCategoriesAction(String urlConditions) {
		super(urlConditions);
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		List<Category> categories = null;
		if (getURLConditions() != null) categories = FrontController.cs.getCategories(getSQLConditions());
		else categories = FrontController.cs.getCategories();
		request.setAttribute("categories", categories);
	}

}
