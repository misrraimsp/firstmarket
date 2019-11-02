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

		String uri = request.getRequestURI();
		String ctxtPath = request.getContextPath();
		String query = request.getQueryString();
		
		System.out.println();
		System.out.println("requestURI: " + uri + " -- requestQUERY: " + query);
		
		if (uri.equals("/firstmarket/fc/admin")) {
			response.sendRedirect(ctxtPath + "/admin/mainMenu.html");
		}
		else if (uri.equals("/firstmarket/fc/admin/newBook")) {
			response.sendRedirect(ctxtPath + "/admin/newBookForm.jsp");
		}
		else if (uri.equals("/firstmarket/fc/admin/insertBook")) {
			Action insertBookAction = new InsertBookAction();
			insertBookAction.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/bookManager ");
		}
		else if (uri.equals("/firstmarket/fc/admin/updateBook")) {
			Action updateBookAction = new UpdateBookAction();
			updateBookAction.execute(request, response);
			response.sendRedirect(ctxtPath + "/fc/admin/bookManager ");
		}
		else if (uri.equals("/firstmarket/fc/admin/bookManager")) {
			Action getAllBooksAction = new GetAllBooksAction();
			getAllBooksAction.execute(request, response);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/booksManager.jsp");
			dispatcher.forward(request, response);
		}
		else if (uri.equals("/firstmarket/fc/admin/editBook")) {
			Action getBooksByKeyAction = new GetBooksByKeyAction(query);
			getBooksByKeyAction.execute(request, response);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/editBook.jsp");
			dispatcher.forward(request, response);
		}
		else {
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
		//
		//System.out.println("requestCONTXTPTH: " + contxtpath);
		*/
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
