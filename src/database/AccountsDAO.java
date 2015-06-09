/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import data.AccountData;


/**
 * Manages Accounts
 */
public class AccountsDAO {
	public static final int ACCOUNT_NOT_FOUND = -1;
	
	private static final String IDACCOUNT = "idAccount";
	private static final String NETID = "netid";
	private static final String FIRSTNAME = "firstName";
	private static final String LASTNAME = "lastName";
	
	private static final String CREATE_ACCOUNT_SQL = "INSERT INTO accounts (netid, password, firstName, lastName) VALUES ('%s', '%s', '%s', '%s')";
	private static final String GET_IDACCOUNT_NET_PASS_SQL = "SELECT idAccount FROM accounts WHERE netid = '%s' AND password = '%s'";
	private static final String GET_IDACCOUNT_NET_SQL = "SELECT idAccount FROM accounts WHERE netid = '%s'";
	private static final String GET_ACCOUNT_SQL = "SELECT idAccount, netid, firstName, lastName FROM accounts WHERE idAccount = %d";
	private static final String GET_ACCOUNTS_SQL = "SELECT idAccount, netid, firstName, lastName FROM accounts";
	private static final String DELETE_ACCOUNT_SQL = "DELETE FROM accounts WHERE idAccount = %d";
	
	private final Database database;
	
	/** Creates an AccountManager */
	AccountsDAO(Database database) {
		this.database = database;
	}
	
	/** Creates an Account */
	public void create(String netid, String password, String firstName, String lastName) throws SQLException {
		database.execute(CREATE_ACCOUNT_SQL, netid, password, firstName, lastName);
	}
	
	/** Gets the id of the account with the given netid and password */
	public int getId(String netid, String password) throws SQLException {
		return database.execute((row, t) -> row.getInt(IDACCOUNT), ACCOUNT_NOT_FOUND, GET_IDACCOUNT_NET_PASS_SQL, netid, password);
	}
	
	/** Gets the id of the account with the given netid */
	public int getId(String netid) throws SQLException {
		return database.execute((row, t) -> row.getInt(IDACCOUNT), ACCOUNT_NOT_FOUND, GET_IDACCOUNT_NET_SQL, netid);
	}
	
	/** Builds an AccountData from a given row */
	private AccountData build(Row row) throws SQLException {
		int idAccount = row.getInt(IDACCOUNT);
		String netid = row.getString(NETID);
		String firstName = row.getString(FIRSTNAME);
		String lastName = row.getString(LASTNAME);
		return new AccountData(idAccount, netid, firstName, lastName);
	}
	
	/** Gets the account for the given token */
	public AccountData get(int idAccount) throws SQLException {
		return database.execute((row, t) -> build(row), null, GET_ACCOUNT_SQL, idAccount);
	}
	
	/** Gets a list of all the accounts */
	public AccountData[] getAccounts() throws SQLException {
		List<AccountData> accounts = database.execute(
				(row, lst) -> {lst.add(build(row)); return lst;}, 
				new LinkedList<AccountData>(), GET_ACCOUNTS_SQL);
		return accounts.toArray(new AccountData[accounts.size()]);
	}
	
	/** Deletes the account with the given netid */
	public void delete(int idAccount) throws SQLException {
		database.execute(DELETE_ACCOUNT_SQL, idAccount);
	}
}
