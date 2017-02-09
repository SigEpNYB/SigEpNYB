/**
 * 
 */
package servlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import servlets.Error.ErrorType;
import exceptions.AccountExistsException;
import exceptions.AccountNotFoundException;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;
import exceptions.RequestExistsException;

/**
 * Servlet implementation of class AccountRequests
 */
public class AccountRequests extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#post(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		String netid = data.getString("netid");
		String password = data.getString("password");
		String firstName = data.getString("firstName");
		String lastName = data.getString("lastName");
		String phone = data.getString("phone");
		try {
			Services.getAccountRequestService().create(netid, password, firstName, lastName, phone);
		} catch (AccountExistsException e) {
			return new Error(ErrorType.ACCOUNT_ALREADY_EXISTS);
		} catch (RequestExistsException e) {
			return new Error(ErrorType.REQUEST_ALREADY_EXISTS);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, java.util.Map)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException, JSONException {
		return Services.getAccountRequestService().get(token);
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#put(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object put(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idRequest = data.getInt("idRequest");
		try {
			Services.getAccountRequestService().accept(token, idRequest);
		} catch (AccountNotFoundException e) {
			throw new MalformedRequestException(String.format("Account request with id: %d does not exist", idRequest));
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idRequest = data.getInt("idRequest");
		try {
			Services.getAccountRequestService().reject(token, idRequest);
		} catch (AccountNotFoundException e) {
			throw new MalformedRequestException(String.format("Account request with id: %d does not exist", idRequest));
		}
		return null;
	}

	
}
