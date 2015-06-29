/**
 * 
 */
package data;

/**
 * The types of duties
 */
public enum DutyType {
	RISKMANAGER(1),
	SOBER(2),
	DRIVER(3),
	SETCLEAN(4);
	
	public final int idType;
	
	/** Creates a duty type */
	DutyType(int id) {
		this.idType = id;
	}
}
