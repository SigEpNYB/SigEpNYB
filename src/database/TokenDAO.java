/**
 * 
 */
package database;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import data.Token;

/**
 * Manages tokens
 */
public class TokenDAO {
	private static final String TOKEN = "token";
	private static final String IDACCOUNT = "idAccount";
	private static final String LOGGEDIN = "loggedIn";
	private static final String LASTACTIVE = "lastActive";
	
	private static final String INSERT_TOKEN_SQL = "INSERT INTO tokens (token, idAccount, loggedIn, lastActive) VALUES ('%s', %d, '%s', '%s')";
	private static final String GET_TOKEN_IDACCOUNT_SQL = "SELECT token, idAccount, loggedIn, lastActive FROM tokens WHERE idAccount = %d";
	private static final String GET_TOKEN_SQL = "SELECT token, idAccount, loggedIn, lastActive FROM tokens WHERE token = '%s'";
	private static final String UPDATE_LASTACTIVE_SQL = "UPDATE tokens SET lastActive='%s' WHERE token='%s'";
	private static final String DELETE_TOKEN_SQL = "DELETE FROM tokens WHERE token = '%s'";
	
	private static final DateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
	
	private final Database database;
	
	/** Creates a new TokenManager */
	TokenDAO(Database database) {
		this.database = database;
	}
	
	/** Adds the given token to the database */
	public void create(String token, int idAccount, Date date) throws SQLException {
		String dateStr = format.format(date);
		database.execute(INSERT_TOKEN_SQL, token, idAccount, dateStr, dateStr);
	}
	
	/** Builds a Token from a row */
	private Token build(Row row) throws SQLException {
		String token = row.getString(TOKEN);
		int idAccount = row.getInt(IDACCOUNT);
		Date loggedIn;
		Date lastActive;
		try {
			loggedIn = format.parse(row.getString(LOGGEDIN));
			lastActive = format.parse(row.getString(LASTACTIVE));
		} catch (ParseException e) {
			throw new SQLException(e);
		}
		return new Token(token, idAccount, loggedIn, lastActive);
	}
	
	/** Gets all the info for the given token */
	public Token get(int idAccount) throws SQLException {
		return database.execute((row, t) -> build(row), null, GET_TOKEN_IDACCOUNT_SQL, idAccount);
	}
	
	/** Gets all the info for the given token */
	public Token get(String token) throws SQLException {
		return database.execute((row, t) -> build(row), null, GET_TOKEN_SQL, token);
	}
	
	/** Updates the last active tag of the token */
	public void update(String token, Date date) throws SQLException {
		database.execute(UPDATE_LASTACTIVE_SQL, format.format(date), token);
	}
	
	/** Deletes the given token */
	public void delete(String token) throws SQLException {
		database.execute(DELETE_TOKEN_SQL, token);
	}

}
