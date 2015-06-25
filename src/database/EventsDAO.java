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
	private static final String CREATE_EVENT_SQL = "INSERT INTO events (title, startTime, endTime, description) VALUES ('%s', '%s', '%s', '%s')";
	private static final String GET_EVENTS_SQL = "SELECT idEvent, title, startTime, endTime, description FROM events "
			+ "WHERE NOT (startTime > '%s' OR endTime < '%s')";
	private static final String DELETE_EVENT_SQL = "DELETE FROM events WHERE idEvent = %d";
	
	private final Database database;
	
	/** Creates a new EventsDAO */
	EventsDAO(Database database) {
		this.database = database;
	}
	
	/** Creates a new Event */
	public void create(String title, Date start, Date end, String description) throws SQLException {
		String startStr = Database.dateToString(start);
		String endStr = Database.dateToString(end);
		database.execute(CREATE_EVENT_SQL, title, startStr, endStr, description);
	}
	
	/** Gets the events that occur between the given start and end dates */
	public Event[] get(Date start, Date end) throws SQLException {
		return database.buildArray(Event.class, GET_EVENTS_SQL, Database.dateToString(start), Database.dateToString(start));
	}
	
	/** Cancels the event with the given idEvent */
	public void cancel(int idEvent) throws SQLException {
		database.execute(DELETE_EVENT_SQL, idEvent);
	}
}
