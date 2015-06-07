/**
 * 
 */
package database;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import data.Event;

/**
 * Manages Events
 */
public class EventsDAO {
	private static final String IDEVENT = "idEvent";
	private static final String TITLE = "title";
	private static final String STARTTIME = "startTime";
	private static final String ENDTIME = "endTime";
	private static final String DESCRIPTION = "description";
	
	private static final String CREATE_EVENT_SQL = "INSERT INTO events (title, startTime, endTime, description) VALUES ('%s', '%s', '%s', '%s')";
	private static final String GET_EVENTS_SQL = "SELECT idEvent, title, startTime, endTime, description FROM events "
			+ "WHERE NOT (startTime > '%s' OR endTime < '%s')";
	private static final String DELETE_EVENT_SQL = "DELETE FROM events WHERE idEvent = %d";
	
	private final Database database;
	private final DateFormat dateFormat;
	
	/** Creates a new EventsDAO */
	EventsDAO(Database database) {
		this.database = database;
		dateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	}
	
	/** Creates a new Event */
	public void create(String title, Date start, Date end, String description) throws SQLException {
		String startStr = dateFormat.format(start);
		String endStr = dateFormat.format(end);
		database.execute(CREATE_EVENT_SQL, title, startStr, endStr, description);
	}
	
	/** Builds an Event from a row */
	private Event build(Row row) throws SQLException {
		int idEvent = row.getInt(IDEVENT);
		String title = row.getString(TITLE);
		Date startTime = null;
		Date endTime = null;
		try {
			startTime = dateFormat.parse(row.getString(STARTTIME));
			endTime = dateFormat.parse(row.getString(ENDTIME));
		} catch (ParseException e) {
			System.err.println("The impossible just happened");
		}
		String description = row.getString(DESCRIPTION);
		
		return new Event(idEvent, title, startTime, endTime, description);
	}
	
	/** Gets the events that occur between the given start and end dates */
	public Event[] get(Date start, Date end) throws SQLException {
		List<Event> events = database.execute(
				(row, lst) -> {lst.add(build(row)); return lst;}, 
				new LinkedList<Event>(), GET_EVENTS_SQL, dateFormat.format(end), dateFormat.format(start));
		return events.toArray(new Event[events.size()]);
	}
	
	/** Cancels the event with the given idEvent */
	public void cancel(int idEvent) throws SQLException {
		database.execute(DELETE_EVENT_SQL, idEvent);
	}
}
