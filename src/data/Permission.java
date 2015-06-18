package data;

/**
 * The different Permissions
 */
public enum Permission {
	GETACCOUNTS(1),
	DELETEACCOUNT(2),
	GETEVENTS(3),
	POSTEVENTS(4),
	DELETEEVENTS(5),
	GETACCOUNTREQUESTS(6),
	ACCEPTREQUEST(7),
	REJECTREQUEST(8);
	
	public final int id;
	
	/** Create Permission */
	Permission(int id) {
		this.id = id;
	}
}