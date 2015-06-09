package servlets;

import javax.servlet.annotation.WebServlet;

import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Roles
 */
@WebServlet("/Roles")
public class Roles extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, JSONObject data) throws ClientBoundException {
		return Services.getPageService().get(token);
	}

}
