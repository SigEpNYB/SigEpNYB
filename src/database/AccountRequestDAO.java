/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import data.AccountRequest;
import data.FullAccountRequest;

/**
 * Manages account requests
 */
public class AccountRequestDAO {
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
	
	/** Gets an account request with the given id */
	public FullAccountRequest get(int idRequest) throws SQLException {
		return database.execute((row, t) -> row.build(FullAccountRequest.class), null, GET_REQUEST_SQL, idRequest);
	}
	
	/** Gets all of the requests */
	public AccountRequest[] getAll() throws SQLException {
		List<AccountRequest> requests = database.execute(
				(row, lst) -> {lst.add(row.build(AccountRequest.class)); return lst;}, 
				new LinkedList<AccountRequest>(), GET_REQUESTS_SQL);
		return requests.toArray(new AccountRequest[requests.size()]);
	}
	
	/** Deletes the account with the given netid */
	public void delete(int idRequest) throws SQLException {
		database.execute(DELETE_REQUEST_SQL, idRequest);
	}
}
