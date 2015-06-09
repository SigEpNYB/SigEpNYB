/**
 * 
 */
package servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import exceptions.ClientBoundException;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.InvalidTokenException;
import exceptions.MalformedRequestException;
import exceptions.PermissionDeniedException;

/**
 * An abstract servlet
 */
public class FratServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, data) -> post(token, data));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, data) -> get(token, data));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, data) -> put(token, data));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, data) -> delete(token, data));
	}

	/** Executes a post */
	protected Object post(String token, JSONObject data) throws ClientBoundException {return null;}

	/** Executes a post */
	protected Object get(String token, JSONObject data) throws ClientBoundException {return null;}

	/** Executes a post */
	protected Object put(String token, JSONObject data) throws ClientBoundException {return null;}

	/** Executes a post */
	protected Object delete(String token, JSONObject data) throws ClientBoundException {return null;}
	
	
	/** processes a method */
	private void process(HttpServletRequest req, HttpServletResponse resp, ServletMethod method) {
		try {
			String token = req.getHeader("Auth");
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(req.getInputStream()));
			String input = "";
			String line = "";
			while ((line = reader.readLine()) != null) input += line + "\n";
			
			JSONObject data;
			if (!input.equals("")) {
				data = new JSONObject(input);
			} else {
				data = new JSONObject();
			}
			
			resp.setContentType("text/json");
			Writer writer = resp.getWriter();
			
			try {
				Object result = method.exec(token, data);
				if (result != null) {
					if (result.getClass().isArray()) {
						JSONArray json = new JSONArray(result);
						json.write(writer);
					} else {
						JSONObject json = new JSONObject(result);
						json.write(writer);
					}
				}
			} catch (MalformedRequestException e) {
				resp.sendError(400, "Malformed request");
			} catch (InternalServerException e) {
				resp.sendError(500);
			} catch (InvalidTokenException e) {
				resp.sendRedirect("/Fratsite/index.html");
			} catch (PermissionDeniedException e) {
				resp.sendError(401, "Current user does not have correct permissions");
			} catch (InvalidCredentialsException e) {
				resp.sendError(401, "Invalid Credentials");
			} catch (ClientBoundException e) {
				e.printStackTrace();
				resp.sendError(500);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				resp.sendError(500);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 
	 * A method run by a servlet
	 */
	private interface ServletMethod {
		
		/** Executes the method */
		public Object exec(String token, JSONObject data) throws ClientBoundException;
	}
}
