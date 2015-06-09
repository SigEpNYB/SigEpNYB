package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import services.Services;
import data.Pages;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;

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
		
		try {
			Pages pages = Services.getPageService().get(token);
			JSONArray output = pages.toJSON();
			output.write(writer);
		} catch (InternalServerException e) {
			response.sendError(500);
		} catch (InvalidTokenException e) {
			response.sendRedirect("/Fratsite/index.html");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
