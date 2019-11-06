package pfg.firstmarket.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.actions.Action;
import pfg.firstmarket.control.actions.DeleteBookAction;
import pfg.firstmarket.control.actions.GetBooksAction;
import pfg.firstmarket.control.actions.InitialSetupAction;
import pfg.firstmarket.control.actions.InsertBookAction;
import pfg.firstmarket.control.actions.UpdateBookAction;
import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;

@WebServlet("/fc/*")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static DAO db = new DAOjdbc();
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Action action = null;
		
		String uri = request.getRequestURI();
		String ctxtPath = request.getContextPath();
		String query = request.getQueryString();
		
		System.out.println();
		System.out.println("requestURI: " + uri + " -- requestQUERY: " + query);
		
		
		switch (uri) {
		case "/firstmarket/fc/initialSetup":
			action = new InitialSetupAction(db);
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin");
			break;
		case "/firstmarket/fc/admin":
			request.getRequestDispatcher("/admin/mainMenu.html").forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/booksManager":
			action = new GetBooksAction(db);
			action.execute(request, response);
			request.getRequestDispatcher("/admin/books/booksManager.jsp").forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/newBook":
			request.getRequestDispatcher("/admin/books/newBookForm.jsp").forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/insertBook":
			action = new InsertBookAction(db);
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager");
			break;
		case "/firstmarket/fc/admin/books/updateBook":
			action = new UpdateBookAction(db);
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager");
			break;
		case "/firstmarket/fc/admin/books/editBook":
			action = new GetBooksAction(db, query);
			action.execute(request, response);
			request.getRequestDispatcher("/admin/books/editBook.jsp").forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/deleteBook":
			action = new DeleteBookAction(db);
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager");
			break;
		case "/firstmarket/fc/admin/categories/categoriesManager":
			request.getRequestDispatcher("/admin/categories/categoriesManager.jsp").forward(request, response);
			break;
		default:
			response.getWriter().append("Served at: ").append(ctxtPath);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
