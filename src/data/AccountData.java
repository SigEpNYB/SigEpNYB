/**
 * 
 */
package data;

/**
 * Stores information about an account
 */
public class AccountData {
	private final int idAccount;
	private final String netid;
	private final String firstName;
	private final String lastName;
	private final String phone;
	
	/** Creates an Account */
	public AccountData(int idAccount, String netid, String firstName, String lastName, String phone) {
		this.idAccount = idAccount;
		this.netid = netid;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return idAccount;
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
	
	/**
	 * @return the phone number
	 */
	public String getPhone() {
		return phone;
	}
}
