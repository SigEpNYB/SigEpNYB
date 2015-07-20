/**
 * 
 */
package services;

import iservice.RestrictedService;
import data.Duty;
import data.DutyType;
import data.Permission;
import database.DutiesDAO;
import exceptions.EventNotFoundException;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * The logic behind duties
 */
public class DutyService extends RestrictedService<DutiesDAO> {
	private final EventService eventService;
	
	/** Creates a new duty service */
	protected DutyService(DutiesDAO dao, TokenService tokenService, PermissionService permissionService, EventService eventService) {
		super(dao, tokenService, permissionService);
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
