package servlets;

import java.util.Map;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;
import exceptions.MalformedRequestException;

public class Announcements extends FratServlet {
  private static final long serialVersionUID = 1L;

  /* (non-Javadoc)
   * @see servlets.FratServlet#post(java.lang.String, java.util.Map, org.json.JSONObject)
   */
  @Override
  protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {
    String body = data.getString("body");
    
    Date now = new Date();
    Services.getAnnouncementService().create(token, body, now);
    
    return null;
  }
}