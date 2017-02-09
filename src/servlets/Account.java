package servlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Account
 */
public class Account extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		if (urlParams.containsKey("showPermissions") && urlParams.get("showPermissions").equals("true")) {
			return Services.getAccountService().getPermissions(token);
		} else {
			return Services.getAccountService().getAccount(token);
		}
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#put(java.lang.String, java.util.map, org.json.JSONObject)
	 */
	@Override
	protected Object put(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		String oldPassword = data.getString("oldPassword");
		String newPassword = data.getString("newPassword");

		Services.getAccountService().changePassword(token, oldPassword, newPassword);
		return null;
	}
}
