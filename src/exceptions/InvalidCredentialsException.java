/**
 * 
 */
package exceptions;

/**
 * Thrown when a login is attempted with invalid credentials
 */
public class InvalidCredentialsException extends ClientBoundException {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new InvalidCredentialsException */
	public InvalidCredentialsException() {
		super("Invalid Credentials");
	}
}
