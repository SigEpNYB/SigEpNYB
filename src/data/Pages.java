/**
 * 
 */
package data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Contains information about pages
 */
public class Pages {
	private final Map<String, List<Link>> links;
	
	/** Creates a new Pages */
	public Pages() {
		links = new HashMap<>();
	}
	
	/** Adds a link */
	public Pages addLink(String role, String pageName, String href) {
		List<Link> roleLinks;
		if (links.containsKey(role)) {
			roleLinks = links.get(role);
		} else {
			roleLinks = new LinkedList<>();
			links.put(role, roleLinks);
		}
		roleLinks.add(new Link(pageName, href));
		return this;
	}
	
	/** Translates to json */
	public JSONArray toJSON() {
		JSONArray json = new JSONArray();
		for (Entry<String, List<Link>> entry : links.entrySet()) {
			JSONObject role = new JSONObject();
			role.put("name", entry.getKey());
			
			JSONArray roleLinks = new JSONArray();
			for (Link link : entry.getValue()) {
				roleLinks.put(link.toJSON());
			}
			role.put("links", roleLinks);
			
			json.put(role);
		}
		
		return json;
	}
}
