/**
 * 
 */
package services;

import data.Pages;
import database.PagesDAO;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;

/**
 * Logic behind pages
 */
public class PageService extends Service<PagesDAO> {

	/** Creates a PageService */
	PageService(PagesDAO dao) {
		super(dao);
	}
	
	/** Gets the pages for the given user */
	public Pages get(String token) throws InternalServerException, InvalidTokenException {
		return run(token, (dao, tokenInfo) -> {
			return dao.get(tokenInfo.getIdAccount());
		})
		.unwrap();
	}

}
