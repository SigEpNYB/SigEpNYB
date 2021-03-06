/**
 * 
 */
package exceptions;

/**
 * Thrown when there was an internal server error
 */
public class InternalServerException extends ClientBoundException {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new InternalServerException */
	public InternalServerException() {
		super("Internal Server Error");
	}
}
