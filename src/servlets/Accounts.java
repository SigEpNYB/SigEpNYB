package servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import database.DatabaseOld;

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
		Writer writer = response.getWriter();
		
		String token = request.getHeader("Auth");
		try (DatabaseOld db = new DatabaseOld()) {
			if (db.hasPermission(token, 1)) {
				JSONArray json = new JSONArray(db.getAccounts());
				json.write(writer);
			}
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
		try (DatabaseOld db = new DatabaseOld()) {
			if (db.hasPermission(token, 2)) {
				db.createAccount(account.getString("netid"), account.getString("firstName"), account.getString("lastName"));
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Auth");
		JSONObject account = new JSONObject(new JSONTokener(request.getInputStream()));
		try (DatabaseOld db = new DatabaseOld()) {
			if (db.hasPermission(token, 3)) {
				db.deleteAccount(account.getString("netid"));
			}
		} catch (Exception e) {
			response.sendError(500, e.getMessage());
			e.printStackTrace();
		}
	}
}
