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
	
	private static final String GET_PERMISSIONS_SQL = "SELECT permissions.name FROM permissions "
			+ "LEFT JOIN role_permissions ON permissions.idPermission = role_permissions.idPermission "
			+ "LEFT JOIN user_roles ON role_permissions.idRole = user_roles.idRole "
			+ "WHERE user_roles.idAccount = %d";
	
	private final Database database;
	
	/** Creates a new PermissionManager */
	PermissionDAO(Database database) {
		this.database = database;
	}
	
	/** Checks if the given token has the requested permission */
	public boolean has(int idAccount, Permission permission) throws SQLException {
		return database.execute((r, t) -> true, false, GET_PERMISSION_SQL, idAccount, permission.id);
	}
	
	/** Gets the list of permissions for the given user */
	public String[] get(int idAccount) throws SQLException {
		return database.buildArray(String.class, GET_PERMISSIONS_SQL, idAccount);
	}

}
