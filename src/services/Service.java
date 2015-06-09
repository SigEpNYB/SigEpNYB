/**
 * 
 */
package services;

import data.Permission;
import data.Token;
import exceptionhandling.ExceptionProcessor;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;
import exceptions.TokenNotFoundException;

/**
 * A generic service.  Server logic is implemented here.
 */
abstract class Service<D> {
	private static final InternalServerException INTERNAL_SERVER_EXCEPTION = new InternalServerException();
	private static final InvalidTokenException INVALID_TOKEN_EXCEPTION = new InvalidTokenException();
	
	private final D dao;
	
	/** Creates a new Service */
	Service(D dao) {
		this.dao = dao;
	}
	
	/** Handles an exception */
	private void handleError(Exception e) {
		e.printStackTrace();
	}
	
	<R> ExceptionProcessor<R, InternalServerException> run(ServiceFunction<D, R> function) {
		try {
			return new ExceptionProcessor<>(function.exec(dao), null, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		} catch (Exception e1) {
			return new ExceptionProcessor<>(null, e1, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		}
	}
	
	ExceptionProcessor<Object, InternalServerException> run(ServiceMethod<D> method) {
		try {
			method.exec(dao);
			return new ExceptionProcessor<>(null, null, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		} catch (Exception e1) {
			return new ExceptionProcessor<>(null, e1, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		}
	}
	
	<R> ExceptionProcessor<R, InternalServerException> run (String token, ServiceTokenFunction<D, R> function) throws InvalidTokenException {
		return run(dao -> {
			Token tokenInfo = Services.getTokenService().getTokenInfo(token);
			return function.exec(dao, tokenInfo);
		})
		.process(TokenNotFoundException.class, INVALID_TOKEN_EXCEPTION);
	}
	
	<R> ExceptionProcessor<R, InternalServerException> run(String token, Permission permission, ServiceTokenFunction<D, R> function) 
			throws PermissionDeniedException, InvalidTokenException {
		return run(token, (dao, tokenInfo) -> {
			if (!Services.getPermissionService().hasPermission(tokenInfo.getIdAccount(), permission)) throw new PermissionDeniedException();
			return function.exec(dao, tokenInfo);
		})
		.process(PermissionDeniedException.class);
	}
}
