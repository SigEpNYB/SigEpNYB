package servlets;

import java.util.Map;

import javax.servlet.annotation.WebServlet;

import org.json.JSONObject;

import services.Services;
import exceptions.ClientBoundException;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends FratServlet {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see servlets.FratServlet#post(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException {
		String netid = data.getString("netid");
		String password = data.getString("password");
		return new Token(Services.getTokenService().login(netid, password));
	}

	/* (non-Javadoc)
	 * @see servlets.FratServlet#delete(java.lang.String, org.json.JSONObject)
	 */
	@Override
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException {
		Services.getTokenService().logout(token);
		return null;
	}
	
	/** An object storing a token */
	public class Token {
		private String token;
		
		/** Creates the token object */
		public Token(String token) {
			this.token = token;
		}
		
		/** Gets the token */
		public String getToken() {
			return token;
		}
	}

}
