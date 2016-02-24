/**
 * 
 */
package database;

import java.sql.SQLException;

import data.Fine;

/**
 * Manages Fines
 */
public class FinesDAO {
	private static final String CREATE_FINE_SQL = "INSERT INTO fines (idAccount, amount, reason) VALUES (?, ?, ?)";
	private static final String GET_FINES_SQL = "SELECT idFine, idAccount, amount, reason FROM fines WHERE idAccount = ?";
	private static final String GET_ALL_FINES_SQL = "SELECT idFine, idAccount, amount, reason FROM fines";
	private static final String DELETE_FINE_SQL = "DELETE FROM fines WHERE idFine = ?";
	
	private final Database database;
	
	/** Creates a FinesDAO */
	FinesDAO(Database database) {
		this.database = database;
	}
	
	/** Creates a fine */
	public void create(int idAccount, float amount, String reason) throws SQLException {
		database.execute(CREATE_FINE_SQL, idAccount, amount, reason);
	}
	
	/** Gets all of the fines for the given user */
	public Fine[] get(int idAccount) throws SQLException {
		return database.buildArray(Fine.class, GET_FINES_SQL, idAccount);
	}
	
	/** Gets all of the fines */
	public Fine[] getAll() throws SQLException {
		return database.buildArray(Fine.class, GET_ALL_FINES_SQL);
	}
	
	/** Deletes the given fine */
	public void delete(int idFine) throws SQLException {
		database.execute(DELETE_FINE_SQL, idFine);
	}
}
