package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.Date;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.Todo;

@RunWith(TreeTestRunner.class)
public class TodoDAOTest {
	private final TodoDAO dao;

	@ParameterVariables("database")
	public TodoDAOTest(IDatabase database) {
		dao = database.getTodoDAO();
	}
	
	@DataFile("testdata/todos.txt")
	@ReturnVariable("idTodo")
	public int start(String description, Date date) throws SQLException {
		int idTodo;
		if (date == null) {
			idTodo = dao.create(description);
		} else {
			idTodo = dao.create(description, date);
		}
		
		assertNotEquals(TodoDAO.CREATE_FAILED, idTodo);
		
		return idTodo;
	}
	
	@DataFile("testdata/todoassignments.txt")
	public void assign(int idTodo, int idAccount) throws SQLException {
		Todo[] todos = dao.get(idAccount);
		for (Todo todo : todos) {
			if (todo.getId() == idTodo) fail();
		}
		
		int numTodos = todos.length;
		dao.assign(idAccount, idTodo);
		todos = dao.get(idAccount);
		assertEquals(numTodos + 1, todos.length);
		
		for (Todo todo : todos) {
			if (todo.getId() == idTodo) return;
		}
		
		fail();
	}
	
	@ParameterVariables("idTodo")
	public void end(int idTodo) throws SQLException {
		dao.delete(idTodo);
	}

}
