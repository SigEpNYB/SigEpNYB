/**
 * 
 */
package exceptions;

/**
 * Thrown when a request is malformed
 */
public class MalformedRequestException extends ClientBoundException {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new MalformedRequestException */
	public MalformedRequestException(String message) {
		super(message);
	}
}
