package servlets;

import javax.servlet.annotation.WebServlet;

import org.json.JSONObject;

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
	protected Object get(String token, JSONObject data) throws ClientBoundException {
		return Services.getAccountService().getAccount(token);
	}

}
