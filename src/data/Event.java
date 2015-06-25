/**
 * 
 */
package data;

import java.util.Date;

/**
 * Holds data for an event
 */
public class Event {
	private final int idEvent;
	private final String title;
	private final Date startTime;
	private final Date endTime;
	private final String description;
	
	/** Creates a new Event */
	public Event(int idEvent, String title, Date startTime, Date endTime, String description) {
		this.idEvent = idEvent;
		this.startTime = startTime;
		this.endTime = endTime;
		this.title = title;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return idEvent;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}

	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
}
