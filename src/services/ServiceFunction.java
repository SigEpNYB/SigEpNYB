/**
 * 
 */
package services;


/**
 * A function run by a service
 */
interface ServiceFunction<D, R> {

	/** executes the function */
	public R exec(D dao) throws Exception;
	
}
