/**
 * 
 */
package servlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.AccountNotFoundException;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;

/**
 * Entry point for fine manipulation
 */
public class Fines extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#post(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idAccount = data.getInt("idAccount");
		float amount = (float) data.getDouble("amount");
		String reason = data.getString("reason");
		
		try {
			Services.getFineService().create(token, idAccount, amount, reason);
		} catch (AccountNotFoundException e) {
			throw new MalformedRequestException("Account with id: " + idAccount + " not found");
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, java.util.Map)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException, JSONException {
		if (urlParams.containsKey("showAll") && urlParams.get("showAll").equals("true")) {
			return Services.getFineService().getAll(token);
		} else {
			return Services.getFineService().get(token);
		}
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idFine = data.getInt("idFine");
		Services.getFineService().remove(token, idFine);
		return null;
	}

}
