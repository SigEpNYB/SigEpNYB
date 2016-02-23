/**
 * 
 */
package database;

import java.sql.SQLException;

import data.AccountData;


/**
 * Manages Accounts
 */
public class AccountsDAO {
	public static final int ACCOUNT_NOT_FOUND = -1;
	
	private static final String IDACCOUNT = "idAccount";
	private static final String PASSWORD = "password";
	
	private static final String CREATE_ACCOUNT_SQL = "INSERT INTO accounts (netid, password, firstName, lastName, phone) VALUES (?, ?, ?, ?, ?)";
	private static final String GET_IDACCOUNT_NET_PASS_SQL = "SELECT idAccount FROM accounts WHERE netid = ? AND password = ?";
	private static final String GET_IDACCOUNT_NET_SQL = "SELECT idAccount FROM accounts WHERE netid = ?";
	private static final String GET_PASSWORD_SQL = "SELECT password FROM accounts WHERE netid = ?";
	private static final String GET_ACCOUNT_SQL = "SELECT idAccount, netid, firstName, lastName, phone FROM accounts WHERE idAccount = ?";
	private static final String GET_ACCOUNTS_SQL = "SELECT idAccount, netid, firstName, lastName, phone FROM accounts";
	private static final String DELETE_ACCOUNT_SQL = "DELETE FROM accounts WHERE idAccount = ?";
	
	private final Database database;
	
	/** Creates an AccountManager */
	AccountsDAO(Database database) {
		this.database = database;
	}
	
	/** Creates an Account */
	public void create(String netid, String password, String firstName, String lastName, String phone) throws SQLException {
		database.execute(CREATE_ACCOUNT_SQL, netid, password, firstName, lastName, phone);
	}
	
	/** Gets the id of the account with the given netid and password */
	public int getId(String netid, String password) throws SQLException {
		return database.execute((row, t) -> row.getInt(IDACCOUNT), ACCOUNT_NOT_FOUND, GET_IDACCOUNT_NET_PASS_SQL, netid, password);
	}
	
	/** Gets the id of the account with the given netid */
	public int getId(String netid) throws SQLException {
		return database.execute((row, t) -> row.getInt(IDACCOUNT), ACCOUNT_NOT_FOUND, GET_IDACCOUNT_NET_SQL, netid);
	}
	
	/** Gets the password for the account with the given netid */
	public String getPassword(String netid) throws SQLException {
		return database.execute((row, t) -> row.getString(PASSWORD), "", GET_PASSWORD_SQL, netid);
	}
	
	/** Gets the account for the given token */
	public AccountData get(int idAccount) throws SQLException {
		return database.build(AccountData.class, GET_ACCOUNT_SQL, idAccount);
	}
	
	/** Gets a list of all the accounts */
	public AccountData[] getAccounts() throws SQLException {
		return database.buildArray(AccountData.class, GET_ACCOUNTS_SQL);
	}
	
	/** Deletes the account with the given netid */
	public void delete(int idAccount) throws SQLException {
		database.execute(DELETE_ACCOUNT_SQL, idAccount);
	}
}
