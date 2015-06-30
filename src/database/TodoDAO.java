/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.Date;

import data.Todo;

/**
 * Manages todos
 */
public class TodoDAO {
	public static final int CREATE_FAILED = -1;
	
	private static final String CREATE_TODO_SQL = "INSERT INTO todos (description) VALUES ('%s')";
	private static final String CREATE_TODO_DUE_SQL = "INSERT INTO todos (description, dueDate) VALUES ('%s', '%s')";
	private static final String ASSIGN_TODO_SQL = "INSERT INTO user_todos (idAccount, idTodo) VALUES (%d, %d)";
	private static final String GET_TODOS_SQL = "SELECT todos.idTodo, todos.description, todos.dueDate FROM user_todos "
			+ "JOIN todos ON user_todos.idTodo = todos.idTodo WHERE user_todos.idAccount = %d";
	private static final String DELETE_TODO_LINKS_SQL = "DELETE FROM user_todos WHERE idTodo = %d";
	private static final String DELETE_TODO_SQL = "DELETE FROM todos WHERE idTodo = %d";
	
	private final Database database;
	
	/** Creates a new TodoDAO */
	TodoDAO(Database database) {
		this.database = database;
	}
	
	/** Creates a todo with the given description and no due date */
	public int create(String description) throws SQLException {
		return database.execute((row, t) -> row.getInt(1), CREATE_FAILED, CREATE_TODO_SQL, description);
	}
	
	/** Creates a todo with the given description and due date */
	public int create(String description, Date dueDate) throws SQLException {
		return database.execute((row, t) -> row.getInt(1), CREATE_FAILED, CREATE_TODO_DUE_SQL, description, Database.dateToString(dueDate));
	}
	
	/** Assigns the given todo to the given user */
	public void assign(int idAccount, int idTodo) throws SQLException {
		database.execute(ASSIGN_TODO_SQL, idAccount, idTodo);
	}
	
	/** Gets all the todos for a given user */
	public Todo[] get(int idAccount) throws SQLException {
		return database.buildArray(Todo.class, GET_TODOS_SQL, idAccount);
	}
	
	/** Deletes the given todo */
	public void delete(int idTodo) throws SQLException {
		database.execute(DELETE_TODO_LINKS_SQL, idTodo);
		database.execute(DELETE_TODO_SQL, idTodo);
	}
}
