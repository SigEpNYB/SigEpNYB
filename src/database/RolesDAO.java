/**
 * 
 */
package database;

import java.sql.SQLException;

/**
 * Manages Roles
 */
public class RolesDAO {
	private static final String INSERT_ROLES_SQL = "INSERT INTO user_roles (idAccount, idRole) VALUES (%d, %d)";
	
	private final Database database;
	
	/** Creates a new RolesDAO */
	RolesDAO(Database database) {
		this.database = database;
	}
	
	/** Assigns the role to the account with the given idAccount */
	public void assign(int idAccount, Role role) throws SQLException {
		database.execute(INSERT_ROLES_SQL, idAccount, role.id);
	}
	
	/** The different Roles */
	public enum Role {
		BROTHER(1),
		PRESIDENT(2),
		VPPROGRAMMING(3);
		
		private final int id;
		
		Role(int id) {
			this.id = id;
		}
	}
}
