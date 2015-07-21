/**
 * 
 */
package servlets;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import data.DutyType;
import exceptions.AccountNotFoundException;
import exceptions.ClientBoundException;
import exceptions.EventNotFoundException;
import exceptions.MalformedRequestException;

/**
 * Entry point for duty manipulation
 */
public class Duties extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#post(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idEvent = data.getInt("idEvent");
		String typeStr = data.getString("type");
		DutyType type;
		try {
			type = DutyType.valueOf(typeStr);
		} catch (Throwable t) {
			String acceptableTypes = "";
			for (DutyType dutyType : DutyType.values()) {
				acceptableTypes += dutyType.name() + ", ";
			}
			acceptableTypes = acceptableTypes.substring(0, acceptableTypes.length() - 2);
			throw new MalformedRequestException("There is no duty type: " + typeStr + ". Acceptable types are: " + acceptableTypes);
		}
		
		try {
			Services.getDutyService().create(token, idEvent, type);
		} catch (EventNotFoundException e) {
			throw new MalformedRequestException("Event with id: " + idEvent + " not found");
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, java.util.Map)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException, JSONException {
		String idEventStr = urlParams.get("idEvent");
		if (idEventStr == null) throw new MalformedRequestException("Expected url param: idEvent");
		
		int idEvent;
		try {
			idEvent = Integer.parseInt(idEventStr);
		} catch (NumberFormatException e) {
			throw new MalformedRequestException("url param idEvent must be an integer, got '" + idEventStr + "' instead");
		}
		try {
			return Services.getDutyService().getForEvent(token, idEvent);
		} catch (EventNotFoundException e) {
			throw new MalformedRequestException("Event with id: " + idEvent + " not found");
		}
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#put(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object put(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idDuty = data.getInt("idDuty");
		int idAccount = data.getInt("idAccount");
		try {
			Services.getDutyService().assign(token, idDuty, idAccount);
		} catch (AccountNotFoundException e) {
			throw new MalformedRequestException("Cannot find account with id: " + idAccount);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, java.util.Map, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idDuty = data.getInt("idDuty");
		Services.getDutyService().remove(token, idDuty);
		return null;
	}
	
}
