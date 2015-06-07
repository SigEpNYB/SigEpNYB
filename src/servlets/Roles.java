package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import database.Database;
import database.PermissionDAO.Permission;

/**
 * Servlet implementation class Roles
 */
@WebServlet("/Roles")
public class Roles extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Roles() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/josn");
		PrintWriter writer = response.getWriter();
		
		String token = request.getHeader("Auth");
		try (Database db = new Database()) {
			if (!db.getTokenDAO().isValid(token)) {
				response.sendRedirect("/Fratsite/index.html");
				return;
			}
			
			if (!db.getPermissionDAO().has(token, Permission.GETACCOUNTS)) {
				response.sendError(401, "User does not have correct permission");
				return;
			}
			
			JSONArray output = db.getPagesDAO().get(token).toJSON();
			output.write(writer);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
