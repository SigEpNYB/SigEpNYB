/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.Fine;
import data.Permission;
import database.FinesDAO;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * The logic behind fines
 */
public class FineService extends RestrictedService<FinesDAO> {
	private final AccountsService accountsService;
	
	/** Creates a new fine service */
	protected FineService(FinesDAO dao, TokenService tokenService, PermissionService permissionService, AccountsService accountsService) {
		super(dao, tokenService, permissionService);
		this.accountsService = accountsService;
	}
	
	/** Creates a fine */
	public void create(String token, int idAccount, float amount, String reason) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run(token, Permission.CREATEFINE, dao -> {
			if (!accountsService.exists(idAccount)) throw new AccountNotFoundException();
			dao.create(idAccount, amount, reason);
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Gets the fines for the current user */
	public Fine[] get(String token) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.VIEWFINES, dao -> {
			return dao.get(accountsService.getAccount(token).getId());
		})
		.unwrap();
	}
	
	/** Gets all the fines */
	public Fine[] getAll(String token) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.VIEWALLFINES, dao -> {
			return dao.getAll();
		})
		.unwrap();
	}
	
	/** Removes the given fine */
	public void remove(String token, int idFine) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		run(token, Permission.DELETEFINE, dao -> {
			dao.delete(idFine);
		})
		.unwrap();
	}

}
