package servlets;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.AccountNotFoundException;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;

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
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
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
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		return Services.getAccountService().getAccounts(token);
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		String netid = data.getString("netid");
		try {
			Services.getAccountService().delete(token, netid);
		} catch (AccountNotFoundException e) {
			throw new MalformedRequestException();
		}
		return null;
	}
}
