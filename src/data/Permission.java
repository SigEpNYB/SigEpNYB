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
	REJECTREQUEST(8),
	CREATEDUTY(9),
	ASSIGNDUTY(10),
	REMOVEDUTY(11),
	VIEWDUTIES(12),
	VIEWDUTYSTATS(13),
	CREATEFINE(14),
	VIEWFINES(15),
	VIEWALLFINES(16),
	DELETEFINE(17);
	
	public final int id;
	
	/** Create Permission */
	Permission(int id) {
		this.id = id;
	}
}