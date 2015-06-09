/**
 * 
 */
package database;

import java.sql.SQLException;

import data.Pages;

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
	public Pages get(int idAccount) throws SQLException {
		return database.execute(
				(row, pages) -> pages.addLink(row.getString(ROLENAME), row.getString(PAGENAME), row.getString(HREF)), 
				new Pages(), GET_PAGES_SQL, idAccount);
	}
}
