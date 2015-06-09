/**
 * 
 */
package exceptionhandling;

/**
 * Handles exceptions
 */
public interface ExceptionHandler<E extends Exception> {

	/** Handles the given exception */
	public void handle(E e);
}
