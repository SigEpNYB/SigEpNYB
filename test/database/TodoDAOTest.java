package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.Settings;
import data.Todo;

public class TodoDAOTest {
	Database db;
	TodoDAO dao;

	@Before
	public void setUp() throws Exception {
		Settings settings = Settings.getInstance();
		db = new Database(settings.getDatabaseUser(), settings.getDatabasePassword(), settings.getDatabase());
		dao = db.getTodoDAO();
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}

	@Test
	public void testCreateString() throws SQLException {
		int idTodo = dao.create("bla blab bal");
		dao.assign(3, idTodo);
		Todo[] todos = dao.getTodos(3);
		
		assertEquals(todos.length, 1);
		assertEquals(todos[0].getId(), idTodo);
		assertEquals(todos[0].getDescription(), "bla blab bal");
		assertNull(todos[0].getDueDate());
		
		dao.delete(idTodo);
	}

	@Test
	public void testCreateStringDate() throws SQLException {
		Date now = Calendar.getInstance().getTime();
		
		int idTodo = dao.create("bla blab bal", now);
		dao.assign(3, idTodo);
		Todo[] todos = dao.getTodos(3);
		
		assertEquals(todos.length, 1);
		assertEquals(todos[0].getId(), idTodo);
		assertEquals(todos[0].getDescription(), "bla blab bal");
		assertTrue(Math.abs(todos[0].getDueDate().getTime() - now.getTime()) < 1000);
		
		dao.delete(idTodo);
	}

	@Test
	public void testAssign() throws SQLException {
		int idTodo1 = dao.create("bla blab bal");
		int idTodo2 = dao.create("sdfldkj");
		dao.assign(3, idTodo1);
		dao.assign(2, idTodo1);
		dao.assign(2, idTodo2);
		
		Todo[] todos1 = dao.getTodos(3);
		assertEquals(todos1.length, 1);
		assertEquals(todos1[0].getId(), idTodo1);
		assertEquals(todos1[0].getDescription(), "bla blab bal");
		assertNull(todos1[0].getDueDate());
		
		Todo[] todos2 = dao.getTodos(2);
		assertEquals(todos2.length, 2);
		assertEquals(todos2[0].getId(), idTodo1);
		assertEquals(todos2[0].getDescription(), "bla blab bal");
		assertNull(todos2[0].getDueDate());
		assertEquals(todos2[1].getId(), idTodo2);
		assertEquals(todos2[1].getDescription(), "sdfldkj");
		assertNull(todos2[1].getDueDate());
		
		dao.delete(idTodo1);
		dao.delete(idTodo2);
	}

	@Test
	public void testDelete() throws SQLException {
		int todo1 = dao.create("abc");
		int todo2 = dao.create("123");
		dao.assign(3, todo1);
		dao.assign(2, todo1);
		dao.assign(2, todo2);
		
		dao.delete(todo2);
		assertEquals(dao.getTodos(3).length, 1);
		assertEquals(dao.getTodos(2).length, 1);
		dao.delete(todo1);
		assertEquals(dao.getTodos(3).length, 0);
		assertEquals(dao.getTodos(2).length, 0);
		dao.delete(todo1);
		assertEquals(dao.getTodos(3).length, 0);
		assertEquals(dao.getTodos(2).length, 0);
	}

}
