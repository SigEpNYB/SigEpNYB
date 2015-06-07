/**
 * 
 */
package database;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Manages tokens
 */
public class TokenDAO {
	private static final long TIMEOUT = 60000;
	
	private static final String IDACCOUNT = "idAccount";
	private static final String TOKEN = "token";
	private static final String LASTACTIVE = "lastActive";
	
	private static final String GET_IDACCOUNT_SQL = "SELECT idAccount FROM accounts WHERE netid = '%s' AND password = '%s'";
	private static final String GET_TOKEN_SQL = "SELECT token FROM tokens WHERE idAccount = %d";
	private static final String GET_TOKENTIME_SQL = "SELECT lastActive FROM tokens WHERE token = '%s'";
	private static final String INSERT_TOKEN_SQL = "INSERT INTO tokens (token, idAccount, loggedIn, lastActive) VALUES ('%s', %d, '%s', '%s')";
	private static final String DELETE_TOKEN_SQL = "DELETE FROM tokens WHERE token = '%s'";
	
	private static final DateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
	
	private final Database database;
	
	/** Creates a new TokenManager */
	TokenDAO(Database database) {
		this.database = database;
	}
	
	/** Generates a new token */
	private String generate() {
		return new BigInteger(150, new SecureRandom()).toString(32);
	}
	
	/** Gets the current timestamp */
	private Date getNow() {
		return Calendar.getInstance().getTime();
	}
	
	/** Creates a new token for the given user if it needs to */
	public String create(String netid, String password) throws SQLException {
		int idAccount = database.execute(
				(row, id) -> row.getInt(IDACCOUNT), 
				0, GET_IDACCOUNT_SQL, netid, password);
		
		String token = database.execute(
				(row, t) -> row.getString(TOKEN), 
				null, GET_TOKEN_SQL, idAccount);
		
		if (!isValid(token)) {
			token = generate();
			String now = format.format(getNow());
			database.execute(INSERT_TOKEN_SQL, token, idAccount, now, now);
		}
		
		return token;
	}
	
	/** Checks if a token is valid */
	public boolean isValid(String token) throws SQLException {
		if (token == null) return false;
		
		boolean valid = database.execute(
				(row, t) -> getNow().getTime() - row.getTimestamp(LASTACTIVE).getTime() < TIMEOUT, 
				false, GET_TOKENTIME_SQL, token);
		if (!valid) {
			delete(token);
		}
		
		return valid;
	}
	
	/** Deletes the given token */
	public void delete(String token) throws SQLException {
		database.execute(DELETE_TOKEN_SQL, token);
	}

}
