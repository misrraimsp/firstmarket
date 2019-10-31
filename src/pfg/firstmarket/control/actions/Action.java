package pfg.firstmarket.control.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {

	public void execute(HttpServletRequest reqest, HttpServletResponse response);
}
