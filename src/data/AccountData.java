/**
 * 
 */
package data;

/**
 * Stores information about an account
 */
public class AccountData {
	public final int id;
	public final String netid;
	public final String firstName;
	public final String lastName;
	
	/** Creates an Account */
	public AccountData(int id, String netid, String firstName, String lastName) {
		this.id = id;
		this.netid = netid;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}
