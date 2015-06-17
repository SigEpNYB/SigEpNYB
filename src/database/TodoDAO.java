/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import data.Todo;

/**
 * Manages todos
 */
public class TodoDAO {
	public static final int CREATE_FAILED = -1;
	
	private static String IDTODO = "idTodo";
	private static String DESCRIPTION = "description";
	private static String DUEDATE = "dueDate";
	
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
	
	/** Builds a todo from a row in the database */
	private Todo build(Row row) throws SQLException {
		int idTodo = row.getInt(IDTODO);
		String description = row.getString(DESCRIPTION);
		Date dueDate = Database.stringToDate(row.getString(DUEDATE));
		return new Todo(idTodo, description, dueDate);
	}
	
	/** Gets all the todos for a given user */
	public Todo[] getTodos(int idAccount) throws SQLException {
		List<Todo> todos = database.execute(
				(row, lst) -> {lst.add(build(row)); return lst;}, 
				new LinkedList<Todo>(), GET_TODOS_SQL, idAccount);
		return todos.toArray(new Todo[todos.size()]);
	}
	
	/** Deletes the given todo */
	public void delete(int idTodo) throws SQLException {
		database.execute(DELETE_TODO_LINKS_SQL, idTodo);
		database.execute(DELETE_TODO_SQL, idTodo);
	}
}
