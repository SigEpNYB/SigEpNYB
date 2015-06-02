/**
 * 
 */
package data;

import org.json.JSONObject;

/**
 * Contains information about a link
 */
public class Link {
	public final String pageName;
	public final String href;
	
	/** Creates a new link */
	public Link(String pageName, String href) {
		this.pageName = pageName;
		this.href = href;
	}
	
	/** Translates to json */
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("pageName", pageName);
		json.put("href", href);
		return json;
	}
}
