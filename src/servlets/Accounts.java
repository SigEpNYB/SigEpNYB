package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.json.JSONTokener;

import data.Account;
import database.Database;

/**
 * Servlet implementation class Accounts
 */
@WebServlet("/Accounts")
public class Accounts extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Accounts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json");
		PrintWriter writer = response.getWriter();
		
		String token = request.getHeader("Auth");
		try (Database db = new Database()) {
			Account account = db.getAccount(token);
			
			JSONObject output = new JSONObject();
			output.put("firstName", account.firstName);
			output.put("lastName", account.lastName);
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
		String token = request.getHeader("Auth");
		JSONObject account = new JSONObject(new JSONTokener(request.getInputStream()));
		try (Database db = new Database()) {
			if (db.hasPermission(token, 2)) {
				db.createAccount(account.getString("netid"), account.getString("firstName"), account.getString("lastName"));
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}

}
