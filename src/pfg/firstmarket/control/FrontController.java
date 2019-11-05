package pfg.firstmarket.control;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.actions.*;

@WebServlet("/fc/*")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher dispatcher = null;
		Action action = null;
		
		String uri = request.getRequestURI();
		String ctxtPath = request.getContextPath();
		String query = request.getQueryString();
		
		System.out.println();
		System.out.println("requestURI: " + uri + " -- requestQUERY: " + query);
		
		
		switch (uri) {
		case "/firstmarket/fc/admin":
			dispatcher = request.getRequestDispatcher("/admin/mainMenu.html");
			dispatcher.forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/booksManager":
			action = new GetBooksAction();
			action.execute(request, response);
			dispatcher = request.getRequestDispatcher("/admin/books/booksManager.jsp");
			dispatcher.forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/newBook":
			dispatcher = request.getRequestDispatcher("/admin/books/newBookForm.jsp");
			dispatcher.forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/insertBook":
			action = new InsertBookAction();
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager ");
			break;
		case "/firstmarket/fc/admin/books/updateBook":
			action = new UpdateBookAction();
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager ");
			break;
		case "/firstmarket/fc/admin/books/editBook":
			action = new GetBooksAction(query);
			action.execute(request, response);
			dispatcher = request.getRequestDispatcher("/admin/books/editBook.jsp");
			dispatcher.forward(request, response);
			break;
		case "/firstmarket/fc/admin/books/deleteBook":
			action = new DeleteBookAction();
			action.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/books/booksManager ");
			break;
		case "/firstmarket/fc/admin/categories/categoriesManager":
			action = new GetCategoriesAction();
			action.execute(request, response);
			dispatcher = request.getRequestDispatcher("/admin/categories/categoriesManager.jsp");
			dispatcher.forward(request, response);
			break;
		default:
			response.getWriter().append("Served at: ").append(ctxtPath);
		}
		
		/**
		//StringBuffer url = request.getRequestURL();
		//String servpath = request.getServletPath();
		//String pathinfo = request.getPathInfo();
		//String pathtranslated = request.getPathTranslated();
		//String querystring = request.getQueryString();
		//String contxtpath = request.getContextPath();
		
		//System.out.println("requestURL: " + url);
		//System.out.println("requestSERVPATH: " + servpath);
		//System.out.println("requestPATHINFO: " + pathinfo);
		//System.out.println("requestPATHTRANSL: " + pathtranslated); 
		*/
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
