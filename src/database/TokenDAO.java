/**
 * 
 */
package database;

import java.sql.SQLException;
import java.util.Date;

import data.Token;

/**
 * Manages tokens
 */
public class TokenDAO {
	private static final String INSERT_TOKEN_SQL = "INSERT INTO tokens (token, idAccount, loggedIn, lastActive) VALUES (?, ?, ?, ?)";
	private static final String GET_TOKEN_IDACCOUNT_SQL = "SELECT token, idAccount, loggedIn, lastActive FROM tokens WHERE idAccount = ?";
	private static final String GET_TOKEN_SQL = "SELECT token, idAccount, loggedIn, lastActive FROM tokens WHERE token = ?";
	private static final String UPDATE_LASTACTIVE_SQL = "UPDATE tokens SET lastActive = ? WHERE token = ?";
	private static final String DELETE_TOKEN_SQL = "DELETE FROM tokens WHERE token = ?";
	
	private final Database database;
	
	/** Creates a new TokenManager */
	TokenDAO(Database database) {
		this.database = database;
	}
	
	/** Adds the given token to the database */
	public void create(String token, int idAccount, Date date) throws SQLException {
		String dateStr = Database.dateToString(date);
		database.execute(INSERT_TOKEN_SQL, token, idAccount, dateStr, dateStr);
	}
	
	/** Gets all the info for the given token */
	public Token get(int idAccount) throws SQLException {
		return database.build(Token.class, GET_TOKEN_IDACCOUNT_SQL, idAccount);
	}
	
	/** Gets all the info for the given token */
	public Token get(String token) throws SQLException {
		return database.build(Token.class, GET_TOKEN_SQL, token);
	}
	
	/** Updates the last active tag of the token */
	public void update(String token, Date date) throws SQLException {
		database.execute(UPDATE_LASTACTIVE_SQL, Database.dateToString(date), token);
	}
	
	/** Deletes the given token */
	public void delete(String token) throws SQLException {
		database.execute(DELETE_TOKEN_SQL, token);
	}

}
