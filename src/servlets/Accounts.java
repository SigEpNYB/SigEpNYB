package servlets;

import javax.servlet.annotation.WebServlet;

import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Accounts
 */
@WebServlet("/Accounts")
public class Accounts extends FratServlet {
	private static final long serialVersionUID = 1L;

    /* (non-Javadoc)
     * @see servlets.FratServlet#post(java.lang.String, org.json.JSONObject)
     */
	@Override
	protected Object post(String token, JSONObject data) throws ClientBoundException {
		String netid = data.getString("netid");
		String firstName = data.getString("firstName");
		String lastName = data.getString("lastName");
		Services.getAccountService().create(token, netid, firstName, lastName);
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, JSONObject data) throws ClientBoundException {
		return Services.getAccountService().getAccounts(token);
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, JSONObject data) throws ClientBoundException {
		int idAccount = data.getInt("idAccount");
		Services.getAccountService().delete(token, idAccount);
		return null;
	}
}
