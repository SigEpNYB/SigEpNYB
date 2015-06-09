/**
 * 
 */
package services;

import database.Database;
import database.IDatabase;
import exceptions.InternalServerException;

/**
 * Manages the services
 */
public class Services {
	private static Services uniqueInstance;
	
	private final IDatabase database;
	private final TokenService tokenService;
	private final AccountsService accountsService;
	private final PermissionService permissionService;
	private final EventService eventService;
	private final PageService pageService;
	
	/** This class is a singleton */
	private Services() throws InternalServerException {
		try {
			database = new Database();
			tokenService = new TokenService(database.getTokenDAO());
			accountsService = new AccountsService(database.getAccountsDAO());
			permissionService = new PermissionService(database.getPermissionDAO());
			eventService = new EventService(database.getEventsDAO());
			pageService = new PageService(database.getPagesDAO());
		} catch (Exception e) {
			e.printStackTrace();
			throw new InternalServerException();
		}
	}
	
	/** Gets an instance of Services */
	private static Services getInstance() throws InternalServerException {
		return uniqueInstance == null ? uniqueInstance = new Services() : uniqueInstance;
	}
	
	/** Gets a TokenService */
	public static TokenService getTokenService() throws InternalServerException {
		return getInstance().tokenService;
	}
	
	/** Gets an AccountsService */
	public static AccountsService getAccountService() throws InternalServerException {
		return getInstance().accountsService;
	}
	
	/** Gets a PermissionService */
	public static PermissionService getPermissionService() throws InternalServerException {
		return getInstance().permissionService;
	}
	
	/** Gets an EventsService */
	public static EventService getEventService() throws InternalServerException {
		return getInstance().eventService;
	}
	
	/** Gets a PageService */
	public static PageService getPageService() throws InternalServerException {
		return getInstance().pageService;
	}
}
