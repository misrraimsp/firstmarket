package pfg.firstmarket.control.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;
import pfg.firstmarket.model.Book;

public class GetBooksByKeyAction implements Action {
	
	private String urlConditions;//separated by &
	
	

	public String getUrlConditions() {
		return urlConditions;
	}

	public GetBooksByKeyAction(String urlConditions) {
		super();
		this.urlConditions = urlConditions;
	}

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO db = new DAOjdbc();
		List<String> sqlConditions = getConditionList(urlConditions);
		List<Book> books = db.getBooksByKey(sqlConditions);
		request.setAttribute("books", books);

	}
	
	private List<String> getConditionList(String urlConditions){
		List<String> sqlConditions = new ArrayList<String>(List.of(urlConditions.split("&")));
		return sqlConditions;
	}

}
