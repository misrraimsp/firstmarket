package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.model.Book;

public class DeleteBookAction implements Action {

	private DAO db;

	public DeleteBookAction(DAO db) {
		super();
		this.db = db;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		Book b = new Book(request.getParameter("isbn"));
		db.deleteBook(b);
	}

}
