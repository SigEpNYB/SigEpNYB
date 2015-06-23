/**
 * 
 */
package iservice;

import data.Permission;
import exceptionhandling.ExceptionProcessor;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;
import services.PermissionService;
import services.TokenService;

/**
 * A service which has restricted access for certain methods
 */
public abstract class RestrictedService<D> extends UserService<D> {
	private final PermissionService permissionService;
	
	/** Creates a new RestrictedService */
	protected RestrictedService(D dao, TokenService tokenService, PermissionService permissionService) {
		super(dao, tokenService);
		this.permissionService = permissionService;
	}
	
	protected final <R> ExceptionProcessor<R, InternalServerException> run(String token, Permission permission, ServiceTokenFunction<D, R> function) 
			throws PermissionDeniedException, InvalidTokenException {
		return run(token, (dao, tokenInfo) -> {
			if (!permissionService.hasPermission(tokenInfo.getIdAccount(), permission)) throw new PermissionDeniedException();
			return function.exec(dao, tokenInfo);
		})
		.process(PermissionDeniedException.class);
	}

}
