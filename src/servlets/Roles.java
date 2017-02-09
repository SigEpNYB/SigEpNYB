package servlets;

import java.util.Map;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Roles
 */
public class Roles extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		return Services.getPageService().get(token);
	}

}
