/**
 * 
 */
package servlets;

/**
 * Represents a client error
 */
public class Error {
	private final ErrorType type;
	
	/** Creates a new Error */
	public Error(ErrorType type) {
		this.type = type;
	}
	
	/** Gets the type */
	public String getTypeText() {
		return type.toString();
	}
	
	/** The types of errors */
	public enum ErrorType {
		ACCOUNT_ALREADY_EXISTS,
		REQUEST_ALREADY_EXISTS
	}
}
