package servlets;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Account
 */
@WebServlet("/Account")
public class Account extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		return Services.getAccountService().getAccount(token);
	}

}
