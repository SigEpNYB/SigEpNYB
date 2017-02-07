package data;

import java.util.Date;

/**
 * Contains data about an Announcement
 */
public class Announcement {
  private final int idAnnouncement;
  private final Date postTime;
  private final String body;
  
  /** Creates a new fine */
  public Announcement(int idAnnouncement, Date postTime, String body) {
    this.idAnnouncement = idAnnouncement;
    this.postTime = postTime;
    this.body = body;
  }

  /** Gets the id */
  public int getIdAnnouncement() {
    return idAnnouncement;
  }

  /** Gets the time the announcement was posted */
  public Date getPostTime() {
    return postTime;
  }

  /** Gets the body of fine */
  public String getBody() {
    return body;
  }
}
