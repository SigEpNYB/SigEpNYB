package database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.sql.SQLException;
import java.util.Date;

import org.junit.runner.RunWith;

import com.treetest.junit.DataFile;
import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import data.Token;

@RunWith(TreeTestRunner.class)
public class TokenDAOTest {
	private final TokenDAO dao;
	
	@ParameterVariables("database")
	public TokenDAOTest(IDatabase database) {
		dao = database.getTokenDAO();
	}
	
	@DataFile("testdata/tokens.txt")
	@ReturnVariable("token")
	public String start(String token, int idAccount, Date date) throws SQLException {
		dao.create(token, idAccount, date);
		Token tokenInfo = dao.get(idAccount);
		assertEquals(token, tokenInfo.getToken());
		assertEquals(idAccount, tokenInfo.getIdAccount());
		assertEquals(date, tokenInfo.getLoggedIn());
		assertEquals(date, tokenInfo.getLastActive());
		
		tokenInfo = dao.get(token);
		assertEquals(token, tokenInfo.getToken());
		assertEquals(idAccount, tokenInfo.getIdAccount());
		assertEquals(date, tokenInfo.getLoggedIn());
		assertEquals(date, tokenInfo.getLastActive());
		
		return token;
	}
	
	@DataFile("testdata/tokenupdates.txt")
	public void update(String token, Date date) throws SQLException {
		Token oldInfo = dao.get(token);
		assertNotEquals(oldInfo.getLastActive(), date);
		
		dao.update(token, date);
		Token tokenInfo = dao.get(token);
		assertEquals(oldInfo.getLoggedIn(), tokenInfo.getLoggedIn());
		assertEquals(date, tokenInfo.getLastActive());
	}
	
	@ParameterVariables("token")
	public void end(String token) throws SQLException {
		assertNotNull(token);
		dao.delete(token);
		Token tokenInfo = dao.get(token);
		assertNull(tokenInfo);
	}
	
}
