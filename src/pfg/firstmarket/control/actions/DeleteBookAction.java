package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;
import pfg.firstmarket.model.Book;

public class DeleteBookAction implements Action {


	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO db = new DAOjdbc();
		Book b = new Book(request.getParameter("isbn"));
		db.deleteBook(b);
	}

}
