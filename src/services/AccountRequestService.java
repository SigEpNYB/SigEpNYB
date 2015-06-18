/**
 * 
 */
package services;

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
public class AccountRequestService extends Service<AccountRequestDAO> {

	/** Creates an AccountRequestService */
	AccountRequestService(AccountRequestDAO dao) {
		super(dao);
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
		return run(token, Permission.GETACCOUNTREQUESTS, (dao, tokenInfo) -> {
			return dao.getAll();
		})
		.unwrap();
	}
	
	/** Accepts the given request */
	public void accept(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run(token, Permission.ACCEPTREQUEST, (dao, tokenInfo) -> {
			FullAccountData request = dao.get(idRequest);
			if (request == null) throw new AccountNotFoundException();
			
			dao.delete(idRequest);
			
			String netid = request.getData().getNetid();
			String password = request.getPassword();
			String firstName = request.getData().getFirstName();
			String lastName = request.getData().getLastName();
			Services.getAccountService().create(netid, password, firstName, lastName);
			return null;
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Rejects the given request */
	public void reject(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		run (token, Permission.REJECTREQUEST, (dao, tokenInfo) -> {
			dao.delete(idRequest);
			return null;
		})
		.unwrap();
	}

}
