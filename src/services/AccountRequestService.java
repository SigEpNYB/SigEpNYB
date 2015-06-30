/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.AccountData;
import data.AccountRequest;
import data.FullAccountRequest;
import data.Group;
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
	private final TodoService todoService;
	private final GroupService groupService;
	
	/** Creates an AccountRequestService */
	AccountRequestService(AccountRequestDAO dao, TokenService tokenService, PermissionService permissionService, AccountsService accountService, 
			TodoService todoService, GroupService groupService) {
		super(dao, tokenService, permissionService);
		this.accountsService = accountService;
		this.todoService = todoService;
		this.groupService = groupService;
	}
	
	/** Creates an account request */
	public void create(String netid, String password, String firstName, String lastName) throws InternalServerException {
		run(dao -> {
			AccountData[] accounts = groupService.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS);
			int idTodo = todoService.create(String.format("Review account request for: %s %s (%s)", firstName, lastName, netid), accounts);
			dao.create(netid, password, firstName, lastName, idTodo);
		})
		.unwrap();
	}
	
	/** Gets all of the account requests */
	public AccountRequest[] get(String token) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.GETACCOUNTREQUESTS, dao -> {
			return dao.getAll();
		})
		.unwrap();
	}
	
	/** Accepts the given request */
	public void accept(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run(token, Permission.ACCEPTREQUEST, dao -> {
			FullAccountRequest request = dao.get(idRequest);
			if (request == null) throw new AccountNotFoundException();
			
			dao.delete(idRequest);
			
			String netid = request.getData().getNetid();
			String password = request.getPassword();
			String firstName = request.getData().getFirstName();
			String lastName = request.getData().getLastName();
			accountsService.create(netid, password, firstName, lastName);
			
			todoService.done(request.getData().getIdTodo());
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Rejects the given request */
	public void reject(String token, int idRequest) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run (token, Permission.REJECTREQUEST, dao -> {
			FullAccountRequest request = dao.get(idRequest);
			if (request == null) throw new AccountNotFoundException();
			dao.delete(idRequest);
			todoService.done(request.getData().getIdTodo());
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}

}
