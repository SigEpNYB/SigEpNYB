package servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import services.Services;
import data.AccountData;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

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
		
		try {
			AccountData[] accounts = Services.getAccountService().getAccounts(token);
			JSONArray json = new JSONArray(accounts);
			json.write(writer);
		} catch (InternalServerException e) {
			response.sendError(500);
		} catch (InvalidTokenException e) {
			response.sendRedirect("/Fratsite/index.html");
		} catch (PermissionDeniedException e) {
			response.sendError(401, "Current user does not have correct permissions");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Auth");
		JSONObject account = new JSONObject(new JSONTokener(request.getInputStream()));
		
		try {
			String netid = account.getString("netid");
			String firstName = account.getString("firstName");
			String lastName = account.getString("lastName");
			Services.getAccountService().create(token, netid, firstName, lastName);
		} catch (JSONException e) {
			response.sendError(401, "Malformed request");
		} catch (InternalServerException e) {
			response.sendError(500);
		} catch (InvalidTokenException e) {
			response.sendRedirect("/Fratsite/index.html");
		} catch (PermissionDeniedException e) {
			response.sendError(401, "Current user does not have correct permissions");
		}
	}


	/**
	 * @see HttpServlet#doDelete(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getHeader("Auth");
		JSONObject account = new JSONObject(new JSONTokener(request.getInputStream()));
		
		try {
			int idAccount = account.getInt("idAccount");
			Services.getAccountService().delete(token, idAccount);
		} catch (JSONException e) {
			response.sendError(401, "Malformed request");
		} catch (InternalServerException e) {
			response.sendError(500);
		} catch (InvalidTokenException e) {
			response.sendRedirect("/Fratsite/index.html");
		} catch (PermissionDeniedException e) {
			response.sendError(401, "Current user does not have correct permissions");
		}
	}
}
