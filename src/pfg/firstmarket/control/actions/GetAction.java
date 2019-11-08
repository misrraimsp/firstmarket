package pfg.firstmarket.control.actions;

import java.util.ArrayList;
import java.util.List;

public abstract class GetAction {
	
	private String urlConditions;//param1=val1&param2=val2&param3=val3 (conditions separated by &)
	
	
	protected GetAction() {
		super();
		this.urlConditions = null;
	}
	
	protected GetAction(String urlConditions) {
		super();
		this.urlConditions = urlConditions;
	}

	protected String getURLConditions() {
		return urlConditions;
	}
	
	/**
	 * hay que ver bien esta conversion
	 * 
	 * por ahora lo que hace es convertir una String urlConditions (por ejemplo: param1=val1&param2=val2&param3=val3)
	 * en una lista de String de condiciones SQL (siguiendo con el ejemplo: {para1='value1', para2='value2', para3='value3'})
	 * Es decir, separa las condiciones y añade comillas simples a los valores
	 * @param urlConditions
	 * @return sqlConditions
	 */
	protected List<String> getSQLConditions(){
		List<String> sqlConditions = new ArrayList<String>(List.of(urlConditions.split("&")));
		String[] parts = null;
		for (int i = 0; i < sqlConditions.size(); i++) {
			parts = sqlConditions.get(i).split("=");
			sqlConditions.set(i, parts[0] + "='" + parts[1] + "'");
		}
		return sqlConditions;
	}
	
}
