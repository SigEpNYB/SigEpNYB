/**
 * 
 */
package data;

/**
 * Stores information abount an account request
 */
public class AccountRequest {
	private final int idRequest;
	private final String netid;
	private final String firstName;
	private final String lastName;
	
	/** Creates an Account */
	public AccountRequest(int idRequest, String netid, String firstName, String lastName) {
		this.idRequest = idRequest;
		this.netid = netid;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return idRequest;
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
