/**
 * 
 */
package data;

/**
 * Contains data about a fine
 */
public class Fine {
	private final int idFine;
	private final int idAccount;
	private final double amount;
	private final String reason;
	
	/** Creates a new fine */
	public Fine(int idFine, int idAccount, double amount, String reason) {
		this.idFine = idFine;
		this.idAccount = idAccount;
		this.amount = amount;
		this.reason = reason;
	}

	/** Gets the id*/
	public int getIdFine() {
		return idFine;
	}

	/** Gets the id of the account this fine is assigned to */
	public int getIdAccount() {
		return idAccount;
	}

	/** Gets the amount of fine */
	public double getAmount() {
		return amount;
	}

	/** Gets the reason for the fine */
	public String getReason() {
		return reason;
	}
	
	
}
