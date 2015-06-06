/**
 * 
 */
package database;

import java.sql.SQLException;

/**
 * Manages Permissions
 */
public class PermissionDAO {
	private static final String GET_PERMISSION_SQL = "SELECT tokens.token FROM tokens "
			+ "JOIN user_roles ON tokens.idAccount = user_roles.idAccount "
			+ "JOIN role_permissions ON user_roles.idRole = role_permissions.idRole "
			+ "WHERE tokens.token = '%s' AND role_permissions.idPermission = %d";
	
	private final Database database;
	
	/** Creates a new PermissionManager */
	PermissionDAO(Database database) {
		this.database = database;
	}
	
	/** Checks if the given token has the requested permission */
	public boolean has(String token, Permission permission) throws SQLException {
		return database.execute((r, t) -> true, false, GET_PERMISSION_SQL, token, permission.id);
	}
	
	/** The different Permissions */
	public enum Permission {
		GETACCOUNTS(1),
		POSTACCOUNT(2),
		DELETEACCOUNT(3),
		GETEVENTS(4),
		POSTEVENTS(5),
		DELETEEVENTS(6);
		
		private final int id;
		
		/** Create Permission */
		Permission(int id) {
			this.id = id;
		}
	}

}
