/**
 * 
 */
package database;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Manages Events
 */
public class EventsDAO {
	private static final String CREATE_EVENT_SQL = "INSERT INTO events (title, startTime, endTime, description) VALUES ('%s', '%s', '%s', '%s')";
	
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
}
