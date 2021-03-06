package servlets;

import java.util.Date;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import services.EventService;
import services.Services;
import data.DutyType;
import data.Event;
import exceptions.ClientBoundException;
import exceptions.EventNotFoundException;
import exceptions.InternalServerException;
import exceptions.MalformedRequestException;

/**
 * Servlet implementation class Events
 */
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
		
		if (data.has("duties")) {
			JSONObject duties = data.getJSONObject("duties");
			for (DutyType type : DutyType.values()) {
				String dutyName = type.toString();
				if (duties.has(dutyName)) {
					for (int i = 0; i < duties.getInt(dutyName); i++) {
						try {
							Services.getDutyService().create(token, idEvent, type);
						} catch (EventNotFoundException e) {
							//This should never happen
							throw new InternalServerException();
						}
					}
				}
			}
		}
		
		return new IdEvent(idEvent);
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		EventService eventService = Services.getEventService();
		
		if (urlParams.containsKey("idEvent")) {
			String idstr = urlParams.get("idEvent");
			
			String[] ids = idstr.split(",");
			Event[] events = new Event[ids.length];
			
			for (int i = 0; i < ids.length; i++) {
				events[i] = eventService.get(token, Integer.parseInt(ids[i]));
			}
			
			return events;
		} else {
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
			
			return eventService.get(token, new Date(start), new Date(end));
		}
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
		int idEvent = data.getInt("idEvent");
		Services.getEventService().cancel(token, idEvent);
		return null;
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
