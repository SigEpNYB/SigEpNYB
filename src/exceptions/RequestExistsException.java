/**
 * 
 */
package exceptions;

/**
 * Thrown when an account request is attempted to be created when an account request already exists with the same netid
 */
public class RequestExistsException extends Exception {
	private static final long serialVersionUID = 1L;
}
