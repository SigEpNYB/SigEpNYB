/**
 * 
 */
package exceptions;

/**
 * Thrown when a token used is invalid
 */
public class InvalidTokenException extends ClientBoundException {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new IvalidTokenException */
	public InvalidTokenException(String token) {
		super(String.format("Token: '%s' is not a valid token", token));
	}

}
