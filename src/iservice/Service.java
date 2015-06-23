/**
 * 
 */
package iservice;

import exceptionhandling.ExceptionProcessor;
import exceptions.InternalServerException;

/**
 * A generic service.  Server logic is implemented here.
 */
public abstract class Service<D> {
	private static final InternalServerException INTERNAL_SERVER_EXCEPTION = new InternalServerException();
	
	private final D dao;
	
	/** Creates a new Service */
	protected Service(D dao) {
		this.dao = dao;
	}
	
	/** Handles an exception */
	private void handleError(Exception e) {
		e.printStackTrace();
	}
	
	protected final <R> ExceptionProcessor<R, InternalServerException> run(ServiceFunction<D, R> function) {
		try {
			return new ExceptionProcessor<>(function.exec(dao), null, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		} catch (Exception e1) {
			return new ExceptionProcessor<>(null, e1, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		}
	}
	
	protected final ExceptionProcessor<Object, InternalServerException> run(ServiceMethod<D> method) {
		try {
			method.exec(dao);
			return new ExceptionProcessor<>(null, null, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		} catch (Exception e1) {
			return new ExceptionProcessor<>(null, e1, INTERNAL_SERVER_EXCEPTION, e -> handleError(e));
		}
	}
}
