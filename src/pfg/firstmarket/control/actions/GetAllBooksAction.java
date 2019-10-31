package pfg.firstmarket.control.actions;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;
import pfg.firstmarket.model.Book;

public class GetAllBooksAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO db = new DAOjdbc();
		List<Book> books = db.getAllBooks();
		request.setAttribute("books", books);
	}

}
