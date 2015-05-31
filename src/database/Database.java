/**
 * 
 */
package database;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Properties;


/**
 * Accesses the database
 */
public class Database implements AutoCloseable {
	private final Connection connection;
	
	/** Creates a new database connection 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException */
	public Database() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Properties properties = new Properties();
		properties.put("user", "fratsite");
		properties.put("password", "jeff");
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fratdata", properties);
	}
	
	/** Creates a new token and inserts it in the token table */
	private String newToken(int idAccount) throws SQLException {
		String token = new BigInteger(150, new SecureRandom()).toString(32);
		Timestamp now = getNow();
		
		String sql = String.format("INSERT INTO tokens (token, idAccount, loggedIn, lastActive) VALUES ('%s', %d, ?, ?)", token, idAccount);
		PreparedStatement tokenInsert = connection.prepareStatement(sql);
		tokenInsert.setTimestamp(1, now);
		tokenInsert.setTimestamp(2, now);
		tokenInsert.executeUpdate();
		
		tokenInsert.close();
		return token;
	}
	
	/** Deletes the given token */
	private void deleteToken(String token) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate(String.format("DELETE FROM tokens WHERE token = '%s'", token));
		statement.close();
	}
	
	/** Checks if the given token is still active.  Removes the token if it is not
	 * @throws SQLException */
	public boolean checkToken(String token) throws SQLException {
		boolean tokenActive = false;
		
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(String.format("SELECT * FROM tokens WHERE token = '%s'", token));
		if (results.next()) {
			Timestamp now = getNow();
			Timestamp lastActive = results.getTimestamp("lastActive");
			if (now.getTime() - lastActive.getTime() < 60000) {
				tokenActive = true;
			} else {
				deleteToken(token);
			}
		}
		
		statement.close();
		return tokenActive;
	}
	
	/** Removes all inactive tokens */
	public int cleanTokens() throws SQLException {
		int numCleaned = 0;
		
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery("SELECT * FROM tokens");
		while (results.next()) {
			if (checkToken(results.getString("token"))) {
				numCleaned++;
			}
		}
		
		statement.close();
		return numCleaned;
	}
	
	/** Gets the token for the given account id. If there is no token, returns null 
	 * @throws SQLException */
	private String getToken(int idAccount) throws SQLException {
		String token = null;
		
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(String.format("SELECT * FROM tokens WHERE idAccount = %d", idAccount));
		if (results.next()) {
			token = results.getString("token");
			if (!checkToken(token)) {
				token = null;
			}
		}
		
		statement.close();
		return token;
	}
	
	/** Updates the given token's lastActive field to now 
	 * @throws SQLException */
	private void tokenActivity(String token) throws SQLException {
		PreparedStatement statement = connection.prepareStatement(String.format("UPDATE tokens SET lastActive=? WHERE token='%s'", token));
		statement.setTimestamp(1, getNow());
		statement.executeUpdate();
		
		statement.close();
	}
	
	/** Logs the given user in 
	 * @throws SQLException */
	public String login(String netid, String password) throws SQLException {
		String token = null;
		
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(String.format("SELECT * FROM accounts WHERE netid = '%s' AND password = '%s'", netid, password));
		if (results.next()) {
			int idAccount = results.getInt("idAccount");
			token = getToken(idAccount);
			if (token == null) {
				token = newToken(idAccount);
			} else {
				tokenActivity(token);
			}
		}
		
		statement.close();
		return token;
	}
	
	/** Logs out the user with the given token 
	 * @throws SQLException */
	public void logout(String token) throws SQLException {
		deleteToken(token);
	}
	
	/** Closes the connection to the database 
	 * @throws SQLException */
	@Override
	public void close() throws SQLException {
		connection.close();
	}
	
	/** Gets the current time */
	private Timestamp getNow() {
		return new Timestamp(Calendar.getInstance().getTime().getTime());
	}
}
