package database;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import util.Settings;
import data.AccountRequest;
import data.FullAccountRequest;

public class AccountRequestDAOTest {
	Database db;
	AccountRequestDAO dao;

	@Before
	public void setUp() throws Exception {
		Settings settings = Settings.getInstance();
		db = new Database(settings.getDatabaseUser(), settings.getDatabasePassword(), settings.getDatabase());
		dao = db.getAccountRequestDAO();
	}
	
	@After
	public void tearDown() throws Exception {
		db.close();
	}

	@Test
	public void testCreate() throws SQLException {
		assertEquals(dao.getAll().length, 0);
		
		dao.create("bla", "adfwdf", "wgwf", "Wgww");
		dao.create("fjwfekh", "sfdkwjf", "wfw", "wwwg");
		
		AccountRequest[] requests = dao.getAll();
		assertEquals(requests.length, 2);
		assertEquals(requests[0].getNetid(), "bla");
		assertEquals(requests[0].getFirstName(), "wgwf");
		assertEquals(requests[0].getLastName(), "Wgww");
		assertEquals(requests[1].getNetid(), "fjwfekh");
		assertEquals(requests[1].getFirstName(), "wfw");
		assertEquals(requests[1].getLastName(), "wwwg");
		
		dao.delete(requests[0].getId());
		dao.delete(requests[1].getId());
	}
	
	@Test
	public void testGet() throws SQLException {
		dao.create("a", "b", "c", "d");
		AccountRequest[] accounts = dao.getAll();
		FullAccountRequest request = dao.get(accounts[0].getId());
		
		assertEquals(request.getData().getNetid(), "a");
		assertEquals(request.getPassword(), "b");
		assertEquals(request.getData().getFirstName(), "c");
		assertEquals(request.getData().getLastName(), "d");
		
		dao.delete(request.getData().getId());
	}

	@Test
	public void testDelete() throws SQLException {
		assertEquals(dao.getAll().length, 0);
		
		dao.create("a", "b", "c", "d");
		
		assertEquals(dao.getAll().length, 1);
		
		dao.create("e", "f", "g", "h");
		AccountRequest[] requests = dao.getAll();
		
		assertEquals(requests.length, 2);
		
		dao.delete(requests[0].getId());
		AccountRequest[] requests2 = dao.getAll();
		
		assertEquals(requests2.length, 1);
		assertEquals(requests2[0].getNetid(), requests[1].getNetid());
		
		dao.delete(requests[1].getId());
		
		assertEquals(dao.getAll().length, 0);
	}

}
