/**
 * 
 */
package exceptions;

/**
 * Thrown as an exception towards the client
 */
public class ClientBoundException extends Exception {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new ClientBoundException */
	public ClientBoundException(String message) {
		super(message);
	}
}
