/**
 * 
 */
package data;

import java.util.Date;

/**
 * A todo
 */
public class Todo {
	private final int id;
	private final String description;
	private final Date dueDate;
	
	/** Creates a new Todo */
	public Todo(int id, String description, Date dueDate) {
		this.id = id;
		this.description = description;
		this.dueDate = dueDate;
	}
	
	/** Gets the id */
	public int getId() {
		return id;
	}
	
	/** Gets the description */
	public String getDescription() {
		return description;
	}
	
	/** Gets the due date */
	public Date getDueDate() {
		return dueDate;
	}
}
