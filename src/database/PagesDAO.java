/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import data.Link;
import data.RolePages;

/**
 * Manages the pages
 */
public class PagesDAO {
	private static final String ROLENAME = "rname";
	private static final String PAGENAME = "pname";
	private static final String HREF = "href";
	
	private static final String GET_PAGES_SQL = "SELECT roles.name AS rname, pages.name AS pname, pages.href FROM user_roles "
			+ "JOIN roles ON user_roles.idRole = roles.idRole "
			+ "JOIN role_pages ON roles.idRole = role_pages.idRole "
			+ "JOIN pages ON role_pages.idPage = pages.idPage "
			+ "WHERE user_roles.idAccount = %d";
	
	private final Database database;
	
	/** Creates a new PagesDAO */
	PagesDAO(Database database) {
		this.database = database;
	}
	
	/** Gets the pages for the given user */
	public RolePages[] get(int idAccount) throws SQLException {
		RolePageMaker rolePageMaker = database.execute(
				(row, pages) -> pages.addLink(row.getString(ROLENAME), row.getString(PAGENAME), row.getString(HREF)), 
				new RolePageMaker(), GET_PAGES_SQL, idAccount);
		
		return rolePageMaker.make();
	}
	
	/** Makes a list of RolePages */
	private class RolePageMaker {
		private Map<String, List<Link>> links;
		
		public RolePageMaker() {
			links = new HashMap<>();
		}
		
		/** Adds a link */
		public RolePageMaker addLink(String role, String pageName, String href) {
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
		
		/** Makes the list of RolePages */
		public RolePages[] make() {
			RolePages[] rolePages = new RolePages[links.size()];
			int i = 0;
			for (String role : links.keySet()) {
				List<Link> roleLinks = links.get(role);
				RolePages rolePage = new RolePages(role, roleLinks.toArray(new Link[roleLinks.size()]));
				rolePages[i++] = rolePage;
			}
			return rolePages;
		}
	}
}
