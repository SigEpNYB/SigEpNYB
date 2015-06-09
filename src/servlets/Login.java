package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import services.Services;
import database.Database;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/josn");
		PrintWriter writer = response.getWriter();
		JSONObject output = new JSONObject();
		
		String netid = request.getParameter("netid");
		String password = request.getParameter("password");
		
		try {
			String token = Services.getTokenService().login(netid, password);
			output.put("hasError", false);
			output.put("token", token);
		} catch (InternalServerException e) {
			response.sendError(500);
			return;
		} catch (InvalidCredentialsException e) {
			output.put("hasError", true);
			output.put("error", "Invalid credentials");
		}
		
		output.write(writer);
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Auth");
		try (Database db = new Database()) {
			db.getTokenDAO().delete(token);
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}
	
	

}
