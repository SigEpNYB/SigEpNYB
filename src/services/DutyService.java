/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.Duty;
import data.DutyType;
import data.Permission;
import database.DutiesDAO;
import exceptions.AccountNotFoundException;
import exceptions.EventNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * The logic behind duties
 */
public class DutyService extends RestrictedService<DutiesDAO> {
	private final AccountsService accountsService;
	private final EventService eventService;
	
	/** Creates a new duty service */
	protected DutyService(DutiesDAO dao, TokenService tokenService, PermissionService permissionService, AccountsService accountsService, EventService eventService) {
		super(dao, tokenService, permissionService);
		this.accountsService = accountsService;
		this.eventService = eventService;
	}
	
	/** Creates a duty of the given type */
	public void create(String token, int idEvent, DutyType type) throws InternalServerException, PermissionDeniedException, InvalidTokenException, EventNotFoundException {
		run(token, Permission.CREATEDUTY, dao -> {
			if (!eventService.exists(idEvent)) throw new EventNotFoundException();
			dao.create(idEvent, type);
		})
		.process(EventNotFoundException.class)
		.unwrap();
	}
	
	/** Assigns the given duty to the given user */
	public void assign(String token, int idDuty, int idAccount) throws InternalServerException, PermissionDeniedException, InvalidTokenException, AccountNotFoundException {
		run(token, Permission.ASSIGNDUTY, dao -> {
			if (!accountsService.exists(idAccount)) throw new AccountNotFoundException();
			dao.assign(idDuty, idAccount);
		})
		.process(AccountNotFoundException.class)
		.unwrap();
	}
	
	/** Gets all the duties for a given event */
	public Duty[] getForEvent(String token, int idEvent) throws InternalServerException, PermissionDeniedException, InvalidTokenException, EventNotFoundException {
		return run(token, Permission.VIEWDUTIES, dao -> {
			if (!eventService.exists(idEvent)) throw new EventNotFoundException();
			return dao.getForEvent(idEvent);
		})
		.process(EventNotFoundException.class)
		.unwrap();
	}
	
	/** Removes the given duty */
	public void remove(String token, int idDuty) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		run(token, Permission.REMOVEDUTY, dao -> {
			dao.delete(idDuty);
		})
		.unwrap();
	}

}
