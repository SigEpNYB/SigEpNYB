/**
 * 
 */
package services;

import iservice.RestrictedService;

import java.util.Date;

import data.Event;
import data.Permission;
import database.EventsDAO;
import exceptions.InternalServerException;
import exceptions.InvalidTokenException;
import exceptions.PermissionDeniedException;

/**
 * Logic behind Events
 */
public class EventService extends RestrictedService<EventsDAO> {

	/** Creates an EventService */
	EventService(EventsDAO dao, TokenService tokenService, PermissionService permissionService) {
		super(dao, tokenService, permissionService);
	}

	/** Creates an event */
	public int create(String token, String title, Date startTime, Date endTime, String description) 
			throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.POSTEVENTS, dao -> {
			int idEvent = dao.create(title, startTime, endTime, description);
			if (idEvent == EventsDAO.CREATE_FAILED) throw new InternalServerException();
			return idEvent;
		})
		.unwrap();
	}
	
	/** Checks if an event with the given id exists */
	boolean exists(int idEvent) throws InternalServerException {
		return run(dao -> {
			return dao.exists(idEvent);
		})
		.unwrap();
	}
	
	/** Gets the events occurring between the start and end date */
	public Event[] get(String token, Date start, Date end) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		return run(token, Permission.GETEVENTS, dao -> {
			return dao.get(start, end);
		})
		.unwrap();
	}
	
	/** Cancels the given event */
	public void cancel(String token, int idEvent) throws InternalServerException, PermissionDeniedException, InvalidTokenException {
		run(token, Permission.DELETEEVENTS, dao -> {
			dao.cancel(idEvent);
		})
		.unwrap();
	}
}
