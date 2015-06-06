package servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

import database.Database;
import database.PermissionDAO.Permission;

/**
 * Servlet implementation class Events
 */
@WebServlet("/Events")
public class Events extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Events() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Auth");
		JSONObject event = new JSONObject(new JSONTokener(request.getInputStream()));
		try (Database db = new Database()) {
			if (!db.getTokenDAO().isValid(token)) {
				response.sendError(401, "Invalid token");
				return;
			}
			
			if (!db.getPermissionDAO().has(token, Permission.POSTEVENTS)) {
				response.sendError(401, "User does not have correct permission");
				return;
			}
			
			String title = event.getString("title");
			DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm");
			Date startTime = dateFormat.parse(event.getString("startTime"));
			Date endTime = dateFormat.parse(event.getString("endTime"));
			String description = event.getString("description");
			
			db.getEventsDAO().create(title, startTime, endTime, description);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}

}
