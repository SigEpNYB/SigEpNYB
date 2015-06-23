package iservice;

/**
 * A function run by a service
 */
public interface ServiceFunction<D, R> {

	/** executes the function */
	public R exec(D dao) throws Exception;
}
