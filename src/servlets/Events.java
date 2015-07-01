package servlets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		Date startTime;
		Date endTime;
		try {
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
			startTime = dateFormat.parse(data.getString("startTime"));
			endTime = dateFormat.parse(data.getString("endTime"));
		} catch (ParseException e) {
			throw new MalformedRequestException("Dates must be in format: YYYY-MM-dd'T'HH:mm");
		}
		String description = data.getString("description");
		Services.getEventService().create(token, title, startTime, endTime, description);
		return null;
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#get(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException {
		Date startTime;
		Date endTime;
		try {
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
			startTime = dateFormat.parse(urlParams.get("startTime"));
			endTime = dateFormat.parse(urlParams.get("endTime"));
		} catch (ParseException e) {
			throw new MalformedRequestException("Dates must be in format: YYYY-MM-dd'T'HH:mm");
		}
		return Services.getEventService().get(token, startTime, endTime);
	}
	
	

}
