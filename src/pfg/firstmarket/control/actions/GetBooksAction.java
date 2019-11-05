package pfg.firstmarket.control.actions;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pfg.firstmarket.dao.DAO;
import pfg.firstmarket.dao.DAOjdbc;
import pfg.firstmarket.model.Book;

public class GetBooksAction implements Action {
	
	private String urlConditions;//param1=val1&param2=val2&param3=val3 (conditions separated by &)
	
	public GetBooksAction(String urlConditions) {
		super();
		this.urlConditions = urlConditions;
	}
	
	public GetBooksAction() {
		super();
		this.urlConditions = null;
	}

	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		DAO db = new DAOjdbc();
		List<Book> books = null;
		if (urlConditions != null) {
			List<String> sqlConditions = getConditionList(urlConditions);
			books = db.getBooks(sqlConditions);
		}
		else {
			books = db.getBooks();
		}
		request.setAttribute("books", books);
	}
	
	
	/**
	 * hay que ver bien esta conversion
	 * 
	 * por ahora lo que hace es convertir una String urlConditions (por ejemplo: param1=val1&param2=val2&param3=val3)
	 * en una lista de String de condiciones SQL (siguiendo con el ejemplo: [para1='value1', para2='value2', para3='value3'])
	 * Es decir, separa las condiciones y añade comillas simples a los valores
	 * @param urlConditions
	 * @return
	 */
	private static List<String> getConditionList(String urlConditions){
		List<String> sqlConditions = new ArrayList<String>(List.of(urlConditions.split("&")));
		String[] parts = null;
		for (int i = 0; i < sqlConditions.size(); i++) {
			parts = sqlConditions.get(i).split("=");
			sqlConditions.set(i, parts[0] + "='" + parts[1] + "'");
		}
		return sqlConditions;
	}

}
