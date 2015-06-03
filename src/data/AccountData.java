/**
 * 
 */
package data;

/**
 * Stores information about an account
 */
public class AccountData {
	private final int id;
	private final String netid;
	private final String firstName;
	private final String lastName;
	
	/** Creates an Account */
	public AccountData(int id, String netid, String firstName, String lastName) {
		this.id = id;
		this.netid = netid;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the netid
	 */
	public String getNetid() {
		return netid;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
}
