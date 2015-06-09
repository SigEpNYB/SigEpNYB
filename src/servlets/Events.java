package servlets;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.annotation.WebServlet;

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
	protected Object post(String token, JSONObject data) throws ClientBoundException {
		String title = data.getString("title");
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
		Date startTime;
		Date endTime;
		try {
			startTime = dateFormat.parse(data.getString("startTime"));
			endTime = dateFormat.parse(data.getString("endTime"));
		} catch (ParseException e) {
			throw new MalformedRequestException();
		}
		String description = data.getString("description");
		Services.getEventService().create(token, title, startTime, endTime, description);
		return null;
	}

}
