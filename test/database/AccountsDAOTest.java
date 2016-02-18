package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.sql.SQLException;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.AccountData;

@RunWith(TreeTestRunner.class)
public class AccountsDAOTest {
	private final AccountsDAO dao;
	
	@ParameterVariables("database")
	public AccountsDAOTest(IDatabase database) {
		dao = database.getAccountsDAO();
	}
	
	@DataFile("testdata/accounts.txt")
	@ReturnVariable("idAccount")
	public int start(String netid, String password, String firstName, String lastName) throws SQLException {
		int numAccounts = dao.getAccounts().length;
		dao.create(netid, password, firstName, lastName, "");
		assertEquals(numAccounts + 1, dao.getAccounts().length);
		
		int idAccount = dao.getId(netid);
		AccountData account = dao.get(idAccount);
		assertNotNull(account);
		assertEquals(idAccount, account.getId());
		assertEquals(netid, account.getNetid());
		assertEquals(firstName, account.getFirstName());
		assertEquals(lastName, account.getLastName());
		
		return idAccount;
	}
	
	@DataFile("testdata/getaccounts.txt")
	public void get(String netid, String password, int id) throws SQLException {
		int idAccount = dao.getId(netid, password);
		assertEquals(id, idAccount);
	}
	
	@DataFile("testdata/accounts.txt")
	public void getAll(String netid, String password, String firstName, String lastName) throws SQLException {
		AccountData[] accounts = dao.getAccounts();
		for (AccountData account : accounts) {
			if (account.getNetid().equals(netid) && 
					account.getFirstName().equals(firstName) && 
					account.getLastName().equals(lastName)) return;
		}
		fail();
	}
	
	@ParameterVariables("idAccount")
	public void end(int idAccount) throws SQLException {
		int numAccounts = dao.getAccounts().length;
		dao.delete(idAccount);
		assertEquals(numAccounts - 1, dao.getAccounts().length);
		
		AccountData account = dao.get(idAccount);
		assertNull(account);
	}
}
