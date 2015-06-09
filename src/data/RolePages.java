/**
 * 
 */
package data;


/**
 * Contains information about pages
 */
public class RolePages {
	private final String role;
	private final Link[] links;
	
	/** Creates a new Pages */
	public RolePages(String role, Link[] links) {
		this.role = role;
		this.links = links;
	}
	
	/** Gets the role */
	public String getRole() {
		return role;
	}
	
	/** Gets the liks */
	public Link[] getLinks() {
		return links;
	}
}
