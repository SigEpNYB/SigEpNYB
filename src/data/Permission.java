package data;

/**
 * The different Permissions
 */
public enum Permission {
	GETACCOUNTS(1),
	POSTACCOUNT(2),
	DELETEACCOUNT(3),
	GETEVENTS(4),
	POSTEVENTS(5),
	DELETEEVENTS(6);
	
	public final int id;
	
	/** Create Permission */
	Permission(int id) {
		this.id = id;
	}
}