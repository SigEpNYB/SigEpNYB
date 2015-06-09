/**
 * 
 */
package exceptionhandling;


/**
 * Processes exceptions
 */
public class ExceptionProcessor <T, F extends Exception> {
	private final T data;
	private final Exception e;
	private final F finalException;
	private final ExceptionHandler<Exception> finalHandler;
	
	/** Creates a new ExceptionProcessor */
	public ExceptionProcessor(T data, Exception e, F finalException, ExceptionHandler<Exception> finalHandler) {
		this.data = data;
		this.e = e;
		this.finalException = finalException;
		this.finalHandler = finalHandler;
	}
	
	/** Processes the exception */
	public <E extends Exception> ExceptionProcessor<T, F> process(Class<E> type) throws E {
		return process(type, new ExceptionHandler<E>() {
			@Override
			public void handle(E e) { }
			
		});
	}
	
	/** Processes the exception */
	public <E extends Exception> ExceptionProcessor<T, F> process(Class<E> type, ExceptionHandler<E> handler) throws E {
		if (e != null && type.isInstance(e)) {
			E eo = type.cast(e);
			handler.handle(eo);
			throw eo;
		}
		return this;
	}
	
	/** Processes the exception */
	public <EI, EO extends Exception> ExceptionProcessor<T, F> process(Class<EI> typeIn, EO exceptionOut) throws EO {
		if (e != null && typeIn.isInstance(e)) throw exceptionOut;
		return this;
	}
	
	/** Tries to unwrap the data. Throws the exception if there is one */
	public T unwrap() throws F {
		if (e != null) {
			finalHandler.handle(e);
			throw finalException;
		}
		return data;
	}
}
