/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.AccountData;
import data.FullAccountData;
import data.Permission;
import database.AccountRequestDAO;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * Logic behind account requests
 */
public class AccountRequestService extends RestrictedService<AccountRequestDAO> {
	private final AccountsService accountsService;
	
	/** Creates an AccountRequestService */
	AccountRequestService(AccountRequestDAO dao, TokenService tokenService, PermissionService permissionService, AccountsService accountService) {
		super(dao, tokenService, permissionService);
		this.accountsService = accountService;
	}
	
	/** Creates an account request */
	public void create(String netid, String password, String firstName, String lastName) throws InternalServerException {
		run(dao -> {
			dao.create(netid, password, firstName, lastName);
		})
		.unwrap();
	}
	
	/** Gets all of the account requests */
	public AccountData[] get(String token) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.GETACCOUNTREQUESTS, dao -> {
			return dao.getAll();
		})
		.unwrap();
	}
	
	/** Accepts the given request */
	public void accept(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run(token, Permission.ACCEPTREQUEST, dao -> {
			FullAccountData request = dao.get(idRequest);
			if (request == null) throw new AccountNotFoundException();
			
			dao.delete(idRequest);
			
			String netid = request.getData().getNetid();
			String password = request.getPassword();
			String firstName = request.getData().getFirstName();
			String lastName = request.getData().getLastName();
			accountsService.create(netid, password, firstName, lastName);
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Rejects the given request */
	public void reject(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		run (token, Permission.REJECTREQUEST, dao -> {
			dao.delete(idRequest);
		})
		.unwrap();
	}

}
