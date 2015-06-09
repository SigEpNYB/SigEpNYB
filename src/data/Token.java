/**
 * 
 */
package data;

import java.util.Date;

/**
 * Stores data about a token
 */
public class Token {
	private final String token;
	private final int idAccount;
	private final Date loggedIn;
	private final Date lastActive;
	
	/** Creates a new Token */
	public Token(String token, int idAccount, Date loggedIn, Date lastActive) {
		super();
		this.token = token;
		this.idAccount = idAccount;
		this.loggedIn = loggedIn;
		this.lastActive = lastActive;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @return the idAccount
	 */
	public int getIdAccount() {
		return idAccount;
	}

	/**
	 * @return the loggedIn
	 */
	public Date getLoggedIn() {
		return loggedIn;
	}

	/**
	 * @return the lastActive
	 */
	public Date getLastActive() {
		return lastActive;
	}
}
