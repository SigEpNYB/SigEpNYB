/**
 * 
 */
package data;

import java.util.Date;

/**
 * A todo
 */
public class Todo {
	private final int idTodo;
	private final String description;
	private final Date dueDate;
	
	/** Creates a new Todo */
	public Todo(int idTodo, String description, Date dueDate) {
		this.idTodo = idTodo;
		this.description = description;
		this.dueDate = dueDate;
	}
	
	/** Gets the id */
	public int getId() {
		return idTodo;
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
