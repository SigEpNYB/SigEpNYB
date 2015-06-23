/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.RolePages;
import database.PagesDAO;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;

/**
 * Logic behind pages
 */
public class PageService extends RestrictedService<PagesDAO> {

	/** Creates a PageService */
	PageService(PagesDAO dao, TokenService tokenService, PermissionService permissionService) {
		super(dao, tokenService, permissionService);
	}
	
	/** Gets the pages for the given user */
	public RolePages[] get(String token) throws InternalServerException, InvalidTokenException {
		return run(token, (dao, tokenInfo) -> {
			return dao.get(tokenInfo.getIdAccount());
		})
		.unwrap();
	}

}
