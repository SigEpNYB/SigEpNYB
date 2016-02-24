/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.Date;

import data.Event;

/**
 * Manages Events
 */
public class EventsDAO {
	public static final int CREATE_FAILED = -1;
	
	private static final String CREATE_EVENT_SQL = "INSERT INTO events (title, startTime, endTime, description) VALUES (?, ?, ?, ?)";
	private static final String EVENT_EXISTS_SQL = "SELECT idEvent FROM events WHERE idEvent = ?";
	private static final String GET_EVENT_SQL = "SELECT idEvent, title, startTime, endTime, description FROM events WHERE idEvent = ?";
	private static final String GET_EVENTS_SQL = "SELECT idEvent, title, startTime, endTime, description FROM events "
			+ "WHERE NOT (startTime > ? OR endTime < ?)";
	private static final String DELETE_EVENT_SQL = "DELETE FROM events WHERE idEvent = ?";
	
	private final Database database;
	
	/** Creates a new EventsDAO */
	EventsDAO(Database database) {
		this.database = database;
	}
	
	/** Creates a new Event */
	public int create(String title, Date start, Date end, String description) throws SQLException {
		String startStr = Database.dateToString(start);
		String endStr = Database.dateToString(end);
		return database.execute((row, t) -> row.getInt(1), CREATE_FAILED, CREATE_EVENT_SQL, title, startStr, endStr, description);
	}
	
	/** Checks if an event with the given id exists */
	public boolean exists(int idEvent) throws SQLException {
		return database.execute((row, t) -> true, false, EVENT_EXISTS_SQL, idEvent);
	}
	
	/** Gets the events that occur between the given start and end dates */
	public Event[] get(Date start, Date end) throws SQLException {
		return database.buildArray(Event.class, GET_EVENTS_SQL, Database.dateToString(end), Database.dateToString(start));
	}
	
	/** Gets the event with the given id */
	public Event get(int idEvent) throws SQLException {
		return database.build(Event.class, GET_EVENT_SQL, idEvent);
	}
	
	/** Cancels the event with the given idEvent */
	public void cancel(int idEvent) throws SQLException {
		database.execute(DELETE_EVENT_SQL, idEvent);
	}
}
