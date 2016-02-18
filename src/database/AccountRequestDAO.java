/**
 * 
 */
package database;

import java.sql.SQLException;

import data.AccountRequest;
import data.FullAccountRequest;

/**
 * Manages account requests
 */
public class AccountRequestDAO {
	private static final String CREATE_REQUEST_SQL = "INSERT INTO account_requests (netid, password, firstName, lastName, phone, idTodo) VALUES ('%s', '%s', '%s', '%s', '%s', %d)";
	private static final String HAS_REQUEST_SQL = "SELECT idRequest FROM account_requests WHERE netid = '%s'";
	private static final String GET_REQUEST_SQL = "SELECT idRequest, netid, password, firstName, lastName, phone, idTodo FROM account_requests WHERE idRequest = %d";
	private static final String GET_REQUESTS_SQL = "SELECT idRequest, netid, firstName, lastName, phone, idTodo FROM account_requests";
	private static final String DELETE_REQUEST_SQL = "DELETE FROM account_requests WHERE idRequest = %d";
	
	private final Database database;
	
	/** Creates an AccountManager */
	AccountRequestDAO(Database database) {
		this.database = database;
	}
	
	/** Creates an account request */
	public void create(String netid, String password, String firstName, String lastName, String phone, int idTodo) throws SQLException {
		database.execute(CREATE_REQUEST_SQL, netid, password, firstName, lastName, phone, idTodo);
	}
	
	/** Checks whether there is already an account request with the given netid */
	public boolean has(String netid) throws SQLException {
		return database.execute((r, t) -> true, false, HAS_REQUEST_SQL, netid);
	}
	
	/** Gets an account request with the given id */
	public FullAccountRequest get(int idRequest) throws SQLException {
		return database.build(FullAccountRequest.class, GET_REQUEST_SQL, idRequest);
	}
	
	/** Gets all of the requests */
	public AccountRequest[] getAll() throws SQLException {
		return database.buildArray(AccountRequest.class, GET_REQUESTS_SQL);
	}
	
	/** Deletes the account with the given netid */
	public void delete(int idRequest) throws SQLException {
		database.execute(DELETE_REQUEST_SQL, idRequest);
	}
}
