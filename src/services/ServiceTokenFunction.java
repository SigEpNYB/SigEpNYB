/**
 * 
 */
package services;

import data.Token;

/**
 * A function run by a service that takes in a token
 */
public interface ServiceTokenFunction<D, R> {

	/** Executes the function */
	public R exec(D dao, Token token) throws Exception;
}
