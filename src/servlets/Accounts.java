package servlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.AccountNotFoundException;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;

/**
 * Servlet implementation class Accounts
 */
public class Accounts extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		String idAccountStr = urlParams.get("idAccount");
		if (idAccountStr == null) {
			return Services.getAccountService().getAccounts(token);
		} else {
			try {
				int idAccount = Integer.parseInt(idAccountStr);
				try {
					return Services.getAccountService().getAccount(token, idAccount);
				} catch (AccountNotFoundException e) {
					throw new MalformedRequestException("No account with id: " + idAccount);
				}
			} catch (NumberFormatException e) {
				throw new MalformedRequestException("idAccount must be an integer, got: '" + idAccountStr + "' instead");
			}
		}
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
			throw new MalformedRequestException(String.format("Account with netid: '%s' does not exist", netid));
		}
		return null;
	}
}
