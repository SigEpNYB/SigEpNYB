/**
 * 
 */
package services;

import org.mindrot.jbcrypt.BCrypt;

import iservice.RestrictedService;
import services.EmailService.EmailType;
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
	public void create(String netid, String password, String firstName, String lastName, String phone) throws InternalServerException, AccountExistsException, RequestExistsException {
		run(dao -> {
			AccountData[] accounts = groupService.getMembers(Group.ACCOUNT_REQUEST_REVIEWERS);
			int idTodo = todoService.create(String.format("Review account request for: %s %s (%s)", firstName, lastName, netid), accounts);
			
			if (dao.has(netid)) throw new RequestExistsException();
			if (accountsService.hasAccount(netid)) throw new AccountExistsException();
			dao.create(netid, BCrypt.hashpw(password, BCrypt.gensalt()), firstName, lastName, phone, idTodo);
			
			emailService.send(netid, EmailType.ACCOUNT_REQUESTED);
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
			String phone = request.getData().getPhone();
			accountsService.create(netid, password, firstName, lastName, phone);
			
			todoService.done(request.getData().getIdTodo());
			
			emailService.send(netid, EmailType.ACCOUNT_ACCEPTED);
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
			
			emailService.send(request.getData().getNetid(), EmailType.ACCOUNT_REJECTED);
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}

}
