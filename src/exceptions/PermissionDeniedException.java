/**
 * 
 */
package exceptions;

import data.Permission;

/**
 * Thrown when the user is denied permission
 */
public class PermissionDeniedException extends ClientBoundException {
	private static final long serialVersionUID = 1L;
	
	/** Creates a new PermissionDeniedException */
	public PermissionDeniedException(Permission requiredPermission) {
		super(String.format("User does not have required permission: %s", requiredPermission.toString()));
	}
}
