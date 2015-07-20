/**
 * 
 */
package servlets;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import exceptions.ClientBoundException;
import exceptions.InternalServerException;
import exceptions.InvalidCredentialsException;
import exceptions.InvalidTokenException;
import exceptions.MalformedRequestException;
import exceptions.PermissionDeniedException;

/**
 * An abstract servlet
 */
public abstract class FratServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, urlParams, data) -> post(token, urlParams, data));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, urlParams, data) -> get(token, urlParams));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, urlParams, data) -> put(token, urlParams, data));
	}

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected final void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		process(req, resp, (token, urlParams, data) -> delete(token, urlParams, data));
	}

	/** Executes a post */
	protected Object post(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {return null;}

	/** Executes a post */
	protected Object get(String token, Map<String, String> urlParams) throws ClientBoundException, JSONException {return null;}

	/** Executes a post */
	protected Object put(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {return null;}

	/** Executes a post */
	protected Object delete(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException {return null;}
	
	
	/** processes a method */
	private void process(HttpServletRequest req, HttpServletResponse resp, ServletMethod method) {
		try {
			String token = req.getHeader("Auth");
			
			JSONObject data;
			if (req.getContentLength() > 0) {
				data = new JSONObject(new JSONTokener(new InputStreamReader(req.getInputStream())));
			} else {
				data = new JSONObject();
			}
			
			Map<String, String> urlParams = new HashMap<String, String>();
			Enumeration<String> paramNames = req.getParameterNames();
			while (paramNames.hasMoreElements()) {
				String paramName = paramNames.nextElement();
				urlParams.put(paramName, req.getParameter(paramName));
			}
			
			resp.setContentType("text/json");
			Writer writer = resp.getWriter();
			
			try {
				Object result = method.exec(token, urlParams, data);
				if (result != null) {
					if (result.getClass().isArray()) {
						JSONArray json = serializeArr((Object[]) result);
						json.write(writer);
					} else {
						JSONObject json = serializeObj(result);
						json.write(writer);
					}
				}
			} catch (MalformedRequestException e) {
				resp.sendError(400, e.getMessage());
			} catch (JSONException e) {
				resp.sendError(400, e.getMessage());
			} catch (InternalServerException e) {
				resp.sendError(500, e.getMessage());
			} catch (InvalidTokenException e) {
				resp.sendError(401, e.getMessage());
			} catch (PermissionDeniedException e) {
				resp.sendError(403, e.getMessage());
			} catch (InvalidCredentialsException e) {
				resp.sendError(403, e.getMessage());
			} catch (ClientBoundException e) {
				e.printStackTrace();
				resp.sendError(500, e.getMessage());
			}
		} catch (Throwable t) {
			t.printStackTrace();
			try {
				resp.sendError(500, t.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/** Serializes an object to a json object */
	private JSONObject serializeObj(Object obj) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, JSONException {
		JSONObject ret = new JSONObject();
		for (Method method : obj.getClass().getMethods()) {
			String name = method.getName();
			System.out.println(name);
			if (method.getParameterCount() == 0 && name.matches("get.+") && !Modifier.isStatic(method.getModifiers()) && !name.equals("getClass")) {
				String fieldName = Character.toLowerCase(name.charAt(3)) + name.substring(4);
				Object fieldValue = method.invoke(obj);
				if (fieldValue == null) {
					ret.put(fieldName, fieldValue);
				} else {
					Class<?> valueType = fieldValue.getClass();
					if (Integer.class.equals(valueType)) {
						ret.put(fieldName, (Integer) fieldValue);
					} else if (Double.class.equals(valueType)) {
						ret.put(fieldName, (Double) fieldValue);
					} else if (Boolean.class.equals(valueType)) {
						ret.put(fieldName, (Boolean) fieldValue);
					} else if (String.class.equals(valueType)) {
						ret.put(fieldName, (String) fieldValue);
					} else if (Date.class.equals(valueType)) {
						ret.put(fieldName, ((Date) fieldValue).getTime());
					} else if (valueType.isEnum()) {
						ret.put(fieldName, fieldValue.toString());
					} else {
						ret.put(fieldName, serializeObj(fieldValue));
					}
				}
			}
		}
		return ret;
	}
	
	/** Serializes an object array to a json array */
	private JSONArray serializeArr(Object[] arr) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, JSONException {
		JSONArray ret = new JSONArray();
		for (Object obj : arr) {
			ret.put(serializeObj(obj));
		}
		return ret;
	}
	
	/** 
	 * A method run by a servlet
	 */
	private interface ServletMethod {
		
		/** Executes the method */
		public Object exec(String token, Map<String, String> urlParams, JSONObject data) throws ClientBoundException, JSONException;
	}
}
