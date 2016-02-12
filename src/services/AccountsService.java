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
	private final PermissionService permissionService;
	private final RoleService roleService;
	
	/** Creates an AccountsService */
	AccountsService(AccountsDAO dao, TokenService tokenService, PermissionService permissionService, RoleService roleService) {
		super(dao, tokenService, permissionService);
		this.permissionService = permissionService;
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
	
	/** Checks if the given account exists */
	boolean exists(int idAccount) throws InternalServerException {
		return run(dao -> {
			return dao.get(idAccount) != null;
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
	
	/** Checks if the account with the given netid exists */
	boolean hasAccount(String netid) throws InternalServerException {
		return run(dao -> {
			return dao.getId(netid) != AccountsDAO.ACCOUNT_NOT_FOUND;
		})
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
	
	/** Gets a list of permissions for the user with the given token */
	public String[] getPermissions(String token) throws PermissionDeniedException, InvalidTokenException, InternalServerException {
		return run(token, (dao, t) -> {
			int idAccount = getAccount(token).getId();
			return permissionService.get(idAccount);
		})
		.unwrap();
	}
	
	/** Gets the account data for the given id */
	public AccountData getAccount(String token, int idAccount) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		return run(token, Permission.GETACCOUNTS, dao -> {
			AccountData account = dao.get(idAccount);
			if (account == null) throw new AccountNotFoundException();
			return account;
		})
		.process(AccountNotFoundException.class)
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
