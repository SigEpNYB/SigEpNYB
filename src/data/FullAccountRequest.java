/**
 * 
 */
package data;

/**
 * Contains password info as well as the rest of the account request info
 */
public class FullAccountRequest {
	private final AccountRequest data;
	private final String password;
	
	/** Creates a new FullAccountRequest */
	public FullAccountRequest(AccountRequest data, String password) {
		this.data = data;
		this.password = password;
	}
	
	/** Gets the data */
	public AccountRequest getData() {
		return data;
	}
	
	/** Gets the password */
	public String getPassword() {
		return password;
	}
}
