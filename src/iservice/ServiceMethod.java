package iservice;

/**
 * A method run by a service
 */
public interface ServiceMethod<D> {

	/** Executes the method */
	public void exec(D dao) throws Exception;
}