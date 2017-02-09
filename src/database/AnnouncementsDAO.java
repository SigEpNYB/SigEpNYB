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
  private static final String CREATE_ANNOUNCEMENT_SQL = "INSERT INTO announcements (body, postTime) VALUES (?, ?)";
  private static final String GET_ANNOUNCEMENTS_SQL = "SELECT idAnnouncement, body, postTime FROM announcements "
      + "WHERE NOT (postTime > ? OR postTime < ?)";
  
  private final Database database;
  
  /** Creates a AnnouncementsDAO */
  AnnouncementsDAO(Database database) {
    this.database = database;
  }
  
  /** Creates an announcement */
  public void create(String body, Date postTime) throws SQLException {
    database.execute(CREATE_ANNOUNCEMENT_SQL, body, postTime);
  }

  /** Gets the announcements that occur between the given start and end dates */
  public Announcement[] get(Date start, Date end) throws SQLException {
    return database.buildArray(Announcement.class, GET_ANNOUNCEMENTS_SQL, Database.dateToString(end), Database.dateToString(start));
  }
}
