/**
 * 
 */
package data;

/**
 * Contains data about a duty
 */
public class Duty {
	public static final int UNASSIGNED = 0;
	
	private final int idDuty;
	private final int idEvent;
	private final DutyType type;
	private final int idAccount;
	
	/** Creates a new duty */
	public Duty(int idDuty, int idEvent, DutyType type, int idAccount) {
		this.idDuty = idDuty;
		this.idEvent = idEvent;
		this.type = type;
		this.idAccount = idAccount;
	}
	
	/** Gets the id */
	public int getId() {
		return idDuty;
	}
	
	/** Gets the id of the event */
	public int getIdEvent() {
		return idEvent;
	}
	
	/** Gets the type of the duty */
	public DutyType getType() {
		return type;
	}
	
	/** Gets the id of the account */
	public int getIdAccount() {
		return idAccount;
	}
}
