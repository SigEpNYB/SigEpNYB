/**
 * 
 */
package database;

import java.sql.SQLException;

import data.Permission;


/**
 * Manages Permissions
 */
public class PermissionDAO {
	private static final String GET_PERMISSION_SQL = "SELECT role_permissions.idPermission FROM user_roles "
			+ "JOIN role_permissions ON user_roles.idRole = role_permissions.idRole "
			+ "WHERE user_roles.idAccount = %d AND role_permissions.idPermission = %d";
	
	private final Database database;
	
	/** Creates a new PermissionManager */
	PermissionDAO(Database database) {
		this.database = database;
	}
	
	/** Checks if the given token has the requested permission */
	public boolean has(int idAccount, Permission permission) throws SQLException {
		return database.execute((r, t) -> true, false, GET_PERMISSION_SQL, idAccount, permission.id);
	}

}
