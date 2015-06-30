/**
 * 
 */
package database;

/**
 * Defines the interface for a database
 */
public interface IDatabase extends AutoCloseable {

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
}
