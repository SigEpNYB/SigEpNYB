package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.AccountRequest;
import data.FullAccountRequest;

@RunWith(TreeTestRunner.class)
public class AccountRequestDAOTest {
	private final AccountRequestDAO dao;
	
	@ParameterVariables("database")
	public AccountRequestDAOTest(IDatabase database) {
		dao = database.getAccountRequestDAO();
	}
	
	@DataFile("testdata/accountrequests.txt")
	@ReturnVariable("idRequest")
	public int start(String netid, String password, String firstName, String lastName, int idTodo) throws SQLException {
		int numRequests = dao.getAll().length;
		dao.create(netid, password, firstName, lastName, idTodo);
		AccountRequest[] requests = dao.getAll();
		assertEquals(numRequests + 1, requests.length);
		
		int idRequest = -1;
		for (AccountRequest request : requests) {
			if (request.getNetid().equals(netid)) {
				assertEquals(firstName, request.getFirstName());
				assertEquals(lastName, request.getLastName());
				assertEquals(idTodo, request.getIdTodo());
				idRequest = request.getId();
				break;
			}
		}
		assertTrue(idRequest > 0);
		
		FullAccountRequest request = dao.get(idRequest);
		assertNotNull(request);
		assertEquals(netid, request.getData().getNetid());
		assertEquals(password, request.getPassword());
		assertEquals(firstName, request.getData().getFirstName());
		assertEquals(lastName, request.getData().getLastName());
		assertEquals(idTodo, request.getData().getIdTodo());
		
		return idRequest;
	}
	
	@ParameterVariables("idRequest")
	public void end(int idRequest) throws SQLException {
		int numRequests = dao.getAll().length;
		dao.delete(idRequest);
		AccountRequest[] requests = dao.getAll();
		assertEquals(numRequests - 1, requests.length);
		
		for (AccountRequest request : requests) {
			if (request.getId() == idRequest) fail();
		}
		
		FullAccountRequest request = dao.get(idRequest);
		assertNull(request);
	}

}
