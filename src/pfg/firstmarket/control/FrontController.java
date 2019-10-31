package pfg.firstmarket.control;

import java.io.IOException;

import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.control.actions.Action;
import pfg.firstmarket.control.actions.InsertBookAction;

//@WebServlet("/fc/*")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String uri = request.getRequestURI();
		//StringBuffer url = request.getRequestURL();
		//String servpath = request.getServletPath();
		//String pathinfo = request.getPathInfo();
		//String pathtranslated = request.getPathTranslated();
		//String querystring = request.getQueryString();
		//String contxtpath = request.getContextPath();
		
		System.out.println();
		System.out.println("requestURI: " + uri);
		//System.out.println("requestURL: " + url);
		//System.out.println("requestSERVPATH: " + servpath);
		//System.out.println("requestPATHINFO: " + pathinfo);
		//System.out.println("requestPATHTRANSL: " + pathtranslated);
		//System.out.println("requestQUERYSTRG: " + querystring);
		//System.out.println("requestCONTXTPTH: " + contxtpath);

		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		if (request.getRequestURI().equals("/firstmarket/fc/admin")) {
			response.sendRedirect(request.getContextPath() + "/admin/adminMenu.html");
			//response.sendRedirect("http://localhost:8080/firstmarket/adminMenu.html");
		}
		
		if (request.getRequestURI().equals("/firstmarket/fc/adminWantsNewBook")) {
			response.sendRedirect(request.getContextPath() + "/admin/newBookForm.jsp");
			//response.sendRedirect("http://localhost:8080/firstmarket/adminMenu.html");
		}
		
		if (request.getRequestURI().equals("/firstmarket/fc/insertBook")) {
			Action insertBookAction = new InsertBookAction();
			insertBookAction.execute(request, response);
			response.sendRedirect(request.getContextPath() + "/admin/adminMenu.html");
		}
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
