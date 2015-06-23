/**
 * 
 */
package iservice;

import services.TokenService;
import data.Token;
import exceptionhandling.ExceptionProcessor;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.TokenNotFoundException;

/**
 * A generic service that needs a token in at least one method
 */
public abstract class UserService<D> extends Service<D> {
	private static final InvalidTokenException INVALID_TOKEN_EXCEPTION = new InvalidTokenException();
	
	private final TokenService tokenService;
	
	/** Creates a new UserService */
	protected UserService(D dao, TokenService tokenService) {
		super(dao);
		this.tokenService = tokenService;
	}
	
	protected final <R> ExceptionProcessor<R, InternalServerException> run (String token, ServiceTokenFunction<D, R> function) throws InvalidTokenException {
		return run(dao -> {
			Token tokenInfo = tokenService.getTokenInfo(token);
			return function.exec(dao, tokenInfo);
		})
		.process(TokenNotFoundException.class, INVALID_TOKEN_EXCEPTION);
	}

}
