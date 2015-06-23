/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.AccountData;
import data.Permission;
import data.Role;
import database.AccountsDAO;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * Logic behind accounts
 */
public class AccountsService extends RestrictedService<AccountsDAO> {
	private final RoleService roleService;
	
	/** Creates an AccountsService */
	AccountsService(AccountsDAO dao, TokenService tokenService, PermissionService permissionService, RoleService roleService) {
		super(dao, tokenService, permissionService);
		this.roleService = roleService;
	}

	/** Creates an account */
	void create(String netid, String password, String firstName, String lastName) throws InternalServerException, InvalidTokenException, PermissionDeniedException {
		run(dao -> {
			dao.create(netid, password, firstName, lastName);
			int idAccount = dao.getId(netid);
			roleService.assign(idAccount, Role.BROTHER);
		})
		.unwrap();
	}
	
	/** Gets the account id for the given netid and password */
	public int getId(String netid, String password) throws InternalServerException, AccountNotFoundException {
		return run(dao -> {
			int idAccount = dao.getId(netid, password);
			if (idAccount == AccountsDAO.ACCOUNT_NOT_FOUND) throw new AccountNotFoundException();
			return idAccount;
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Gets the account for the given token */
	public AccountData getAccount(String token) throws InternalServerException, InvalidTokenException {
		return run(token, (dao, tokenInfo) -> {
			int idAccount = tokenInfo.getIdAccount();
			AccountData account = dao.get(idAccount);
			if (account == null) {
				throw new Exception(String.format("Account with given id could not be found but token: '%s' with idAccount: '%d' exists", token, idAccount));
			}
			return account;
		})
		.unwrap();
	}
	
	/** Gets all the accounts */
	public AccountData[] getAccounts(String token) throws InternalServerException, InvalidTokenException, PermissionDeniedException {
		return run(token, Permission.GETACCOUNTS, dao -> {
			return dao.getAccounts();
		})
		.unwrap();
	}
	
	/** Deletes the given account */
	public void delete(String token, String netid) throws InternalServerException, InvalidTokenException, PermissionDeniedException, AccountNotFoundException {
		run(token, Permission.DELETEACCOUNT, dao -> {
			int idAccount = dao.getId(netid);
			roleService.unassignAll(idAccount);
			if (idAccount == AccountsDAO.ACCOUNT_NOT_FOUND) throw new AccountNotFoundException();
			dao.delete(idAccount);
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
}
