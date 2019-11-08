package pfg.firstmarket.control.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.FrontController;
import pfg.firstmarket.model.Book;

public class GetBooksAction extends GetAction implements Action {
	
	public GetBooksAction(String urlConditions) {
		super(urlConditions);
	}
	
	public GetBooksAction() {
		super();
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		List<Book> books = null;
		if (getURLConditions() != null) books = FrontController.db.getBooks(getSQLConditions());
		else books = FrontController.db.getBooks();
		request.setAttribute("books", books);
	}
	
}
