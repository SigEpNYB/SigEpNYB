/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.Date;

import data.Announcement;

/**
 * Manages announcements
 */
public class AnnouncementsDAO {
  private static final String CREATE_ANNOUNCEMENT_SQL = "INSERT INTO fines (body, postTime) VALUES (?, ?)";
  
  private final Database database;
  
  /** Creates a AnnouncementsDAO */
  AnnouncementsDAO(Database database) {
    this.database = database;
  }
  
  /** Creates an announcement */
  public void create(String body, Date postTime) throws SQLException {
    database.execute(CREATE_ANNOUNCEMENT_SQL, body, postTime);
  }
}
