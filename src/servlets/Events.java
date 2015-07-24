package servlets;

import java.util.Date;
import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;

/**
 * Servlet implementation class Events
 */
@WebServlet("/Events")
public class Events extends FratServlet {
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see servlets.FratServlet#post(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		String title = data.getString("title"); 
		Date startTime = new Date(data.getLong("startTime"));
		Date endTime = new Date(data.getLong("endTime"));
		String description = data.getString("description");
		int idEvent = Services.getEventService().create(token, title, startTime, endTime, description);
		return new IdEvent(idEvent);
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		String startStr = urlParams.get("startTime");
		if (startStr == null) throw new MalformedRequestException("Expected url parameter: startTime");
		long start;
		try {
			start = Long.parseLong(startStr);
		} catch (NumberFormatException e) {
			throw new MalformedRequestException("startTime must be an integer");
		}
		
		String endStr = urlParams.get("endTime");
		if (endStr == null) throw new MalformedRequestException("Expected url parameter: endTime");
		long end;
		try {
			end = Long.parseLong(endStr);
		} catch (NumberFormatException e) {
			throw new MalformedRequestException("endTime must be an integer");
		}
		
		return Services.getEventService().get(token, new Date(start), new Date(end));
	}
	
	/** A wrapper class for an event id */
	private class IdEvent {
		private final int idEvent;
		public IdEvent(int idEvent) {
			this.idEvent = idEvent;
		}
		@SuppressWarnings("unused")
		public int getIdEvent() {
			return idEvent;
		}
	}

}
