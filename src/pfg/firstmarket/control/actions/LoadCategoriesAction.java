package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.model.services.CategoryServer;

public class LoadCategoriesAction implements Action {

	private CategoryServer cs;

	public LoadCategoriesAction(CategoryServer cs) {
		super();
		this.cs = cs;
	}
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		cs.loadCategories();
		request.getServletContext().setAttribute("categoryServer", cs);
	}

}
