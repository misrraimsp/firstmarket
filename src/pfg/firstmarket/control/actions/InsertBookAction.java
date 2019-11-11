package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Book;

public class InsertBookAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Book b = new Book(request.getParameter("isbn"),request.getParameter("title"),FrontController.cs.getCategory(request.getParameter("category_id")));
		FrontController.db.insertBook(b);
	}

}
