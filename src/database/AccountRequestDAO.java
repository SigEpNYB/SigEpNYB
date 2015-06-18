/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import data.AccountData;
import data.FullAccountData;

/**
 * Manages account requests
 */
public class AccountRequestDAO {
	private static final String IDREQUEST = "idRequest";
	private static final String NETID = "netid";
	private static final String FIRSTNAME = "firstName";
	private static final String LASTNAME = "lastName";
	private static final String PASSWORD = "password";
	
	private static final String CREATE_REQUEST_SQL = "INSERT INTO account_requests (netid, password, firstName, lastName) VALUES ('%s', '%s', '%s', '%s')";
	private static final String GET_REQUEST_SQL = "SELECT idRequest, netid, password, firstName, lastName FROM account_requests WHERE idRequest = %d";
	private static final String GET_REQUESTS_SQL = "SELECT idRequest, netid, firstName, lastName FROM account_requests";
	private static final String DELETE_REQUEST_SQL = "DELETE FROM account_requests WHERE idRequest = %d";
	
	private final Database database;
	
	/** Creates an AccountManager */
	AccountRequestDAO(Database database) {
		this.database = database;
	}
	
	/** Creates an account request */
	public void create(String netid, String password, String firstName, String lastName) throws SQLException {
		database.execute(CREATE_REQUEST_SQL, netid, password, firstName, lastName);
	}
	
	/** Builds an AccountData from a given row */
	private AccountData build(Row row) throws SQLException {
		int idAccount = row.getInt(IDREQUEST);
		String netid = row.getString(NETID);
		String firstName = row.getString(FIRSTNAME);
		String lastName = row.getString(LASTNAME);
		return new AccountData(idAccount, netid, firstName, lastName);
	}
	
	/** Gets an account request with the given id */
	public FullAccountData get(int idRequest) throws SQLException {
		return database.execute((row, t) -> new FullAccountData(build(row), row.getString(PASSWORD)), null, GET_REQUEST_SQL, idRequest);
	}
	
	/** Gets all of the requests */
	public AccountData[] getAll() throws SQLException {
		List<AccountData> requests = database.execute(
				(row, lst) -> {lst.add(build(row)); return lst;}, 
				new LinkedList<AccountData>(), GET_REQUESTS_SQL);
		return requests.toArray(new AccountData[requests.size()]);
	}
	
	/** Deletes the account with the given netid */
	public void delete(int idRequest) throws SQLException {
		database.execute(DELETE_REQUEST_SQL, idRequest);
	}
}
