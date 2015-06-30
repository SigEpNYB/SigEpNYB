/**
 * 
 */
package database;

import java.sql.SQLException;

import data.AccountData;
import data.Group;

/**
 * Manages groups
 */
public class GroupsDAO {
	private static final String ADD_MEMBER_SQL = "INSERT INTO group_members (idGroup, idAccount) VALUES (%d, %d)";
	private static final String GET_MEMBERS_SQL = "SELECT accounts.idAccount, accounts.netid, accounts.firstName, accounts.lastName FROM group_members "
			+ "JOIN accounts ON group_members.idAccount = accounts.idAccount "
			+ "WHERE group_members.idGroup = %d";
	private static final String REMOVE_MEMBERS_SQL = "DELETE FROM group_members WHERE idGroup = %d AND idAccount = %d";
	
	private final Database database;
	
	/** Creates a GroupsDAO */
	GroupsDAO(Database database) {
		this.database = database;
	}
	
	/** Adds a member to the group */
	public void add(Group group, int idAccount) throws SQLException {
		database.execute(ADD_MEMBER_SQL, group.id, idAccount);
	}
	
	/** Gets the members of the group */
	public AccountData[] getMembers(Group group) throws SQLException {
		return database.buildArray(AccountData.class, GET_MEMBERS_SQL, group.id);
	}
	
	/** Removes a member from the group */
	public void remove(Group group, int idAccount) throws SQLException {
		database.execute(REMOVE_MEMBERS_SQL, group.id, idAccount);
	}
}
