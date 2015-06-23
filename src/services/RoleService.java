/**
 * 
 */
package services;

import iservice.Service;
import data.Role;
import database.RolesDAO;
import exceptions.InternalServerException;

/**
 * Logic behind roles 
 */
public class RoleService extends Service<RolesDAO> {

	/** Creates a new RoleService */
	RoleService(RolesDAO dao) {
		super(dao);
	}
	
	/** Assigns the user the role */
	void assign(int idAccount, Role role) throws InternalServerException {
		run(dao -> {
			dao.assign(idAccount, role);
		})
		.unwrap();
	}
	
	/** Checks whether the user has the given role */
	boolean has(int idAccount, Role role) throws InternalServerException {
		return run(dao -> {
			return dao.has(idAccount, role);
		})
		.unwrap();
	}
	
	/** Unassigns all the roles from the user */
	void unassignAll(int idAccount) throws InternalServerException {
		run(dao -> {
			dao.unassignAll(idAccount);
		})
		.unwrap();
	}

}
