/**
 * 
 */
package services;

import iservice.Service;
import data.Permission;
import database.PermissionDAO;
import exceptions.InternalServerException;

/**
 * Logic behind permissions
 */
public class PermissionService extends Service<PermissionDAO> {
	
	/** Creates a PermissionService */
	PermissionService(PermissionDAO dao) {
		super(dao);
	}

	/** Checks if the given user has the permission in question */
	public boolean hasPermission(int idAccount, Permission permission) throws InternalServerException {
		return run(dao -> {
			return dao.has(idAccount, permission);
		})
		.unwrap();
	}
}
