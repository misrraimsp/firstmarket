package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;

public class LoadCategoriesAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		FrontController.cs.loadCategories();
		request.getServletContext().setAttribute("categoryServer", FrontController.cs);
	}

}
