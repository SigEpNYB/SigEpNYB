/**
 * 
 */
package data;

/**
 * Account request data, including the password
 */
public class FullAccountData {
	private final AccountData data;
	private final String password;
	
	/** Creates a new FullAccountData */
	public FullAccountData(AccountData data, String password) {
		this.data = data;
		this.password = password;
	}
	
	/** Gets the regular account data */
	public AccountData getData() {
		return data;
	}
	
	/** Gets the password */
	public String getPassword() {
		return password;
	}
}
