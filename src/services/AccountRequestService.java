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
import exceptions.AccountExistsException;
import exceptions.AccountNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;
import exceptions.RequestExistsException;

/**
 * Logic behind account requests
 */
public class AccountRequestService extends RestrictedService<AccountRequestDAO> {
	private final AccountsService accountsService;
	private final TodoService todoService;
	private final GroupService groupService;
	private final EmailService emailService;
	
	/** Creates an AccountRequestService */
	AccountRequestService(AccountRequestDAO dao, TokenService tokenService, PermissionService permissionService, AccountsService accountService, 
			TodoService todoService, GroupService groupService, EmailService emailService) {
		super(dao, tokenService, permissionService);
		this.accountsService = accountService;
		this.todoService = todoService;
		this.groupService = groupService;
		this.emailService = emailService;
	}
	
	/** Creates an account request */
	public void create(String netid, String password, String firstName, String lastName) throws InternalServerException, AccountExistsException, RequestExistsException {
		run(dao -> {
			AccountData[] accounts = groupService.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS);
			int idTodo = todoService.create(String.format("Review account request for: %s %s (%s)", firstName, lastName, netid), accounts);
			
			if (dao.has(netid)) throw new RequestExistsException();
			if (accountsService.hasAccount(netid)) throw new AccountExistsException();
			dao.create(netid, password, firstName, lastName, idTodo);
			
			emailService.send(netid, "SigEp Account Requested", "Your Sigma Phi Epsilon account was requested.  "
					+ "The details will be reviewed and you will be notified when a decision is made.");
		})
		.process(RequestExistsException.class)
		.process(AccountExistsException.class)
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
			
			emailService.send(netid, "SigEp Fratsite Account Accepted", 
					"Congradulations, your account request has been accepted.  May your journey be one of unfathomable something.");
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
			
			emailService.send(request.getData().getNetid(), "Sorry, SigEp Fratsite Account Rejected", 
					"We're sorry, you do not appear to be a member of Sigma Phi Epsilon New York Beta chapter, and because of this, your account request was rejected.");
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}

}
