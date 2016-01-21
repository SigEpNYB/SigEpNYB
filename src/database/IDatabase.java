/**
 * 
 */
package database;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Defines the interface for a database
 */
public interface IDatabase extends AutoCloseable {

	/** Creates a database with the main schema with the given name */
	public void createSchema(String name) throws SQLException, IOException;
	
	/** Drops the database with the given name */
	public void dropSchema(String name) throws SQLException;
	
	
	/** Gets the TokenDAO */
	public TokenDAO getTokenDAO();
	
	/** Gets the PermissionDAO */
	public PermissionDAO getPermissionDAO();
	
	/** Gets the AccountsDAO */
	public AccountsDAO getAccountsDAO();
	
	/** Gets the RolesDAO */
	public RolesDAO getRolesDAO();
	
	/** Gets the EventsDAO */
	public EventsDAO getEventsDAO();
	
	/** Gets the PagesDAO */
	public PagesDAO getPagesDAO();
	
	/** Gets the TodoDAO */
	public TodoDAO getTodoDAO();
	
	/** Gets the AccountRequestDAO */
	public AccountRequestDAO getAccountRequestDAO();
	
	/** Gets the DutiesDAO */
	public DutiesDAO getDutiesDAO();
	
	/** Gets the GroupsDAO */
	public GroupsDAO getGroupsDAO();
	
	public FinesDAO getFinesDAO();
}
