/**
 * 
 */
package database;

import java.sql.SQLException;

import data.Duty;
import data.DutyType;

/**
 * Manages duties
 */
public class DutiesDAO {
	private static final String CREATE_DUTY_SQL = "INSERT INTO duties (idEvent, idType) VALUES (%d, %d)";
	private static final String ASSIGN_DUTY_SQL = "UPDATE duties SET idAccount = %d WHERE idDuty = %d";
	private static final String GET_UNASSIGNED_SQL = "SELECT idDuty, idEvent, idType, idAccount FROM duties WHERE idAccount IS NULL";
	private static final String GET_FOR_EVENT_SQL = "SELECT idDuty, idEvent, idType, idAccount FROM duties WHERE idEvent = %d";
	private static final String DELETE_DUTY_SQL = "DELETE FROM duties WHERE idDuty = %d";
	
	private final Database database;
	
	/** Creates a DutiesDAO */
	DutiesDAO(Database database) {
		this.database = database;
	}
	
	/** Creates a duty */
	public void create(int idEvent, DutyType type) throws SQLException {
		database.execute(CREATE_DUTY_SQL, idEvent, type.idType);
	}
	
	/** Assigns the given duty to the guven user */
	public void assign(int idDuty, int idAccount) throws SQLException {
		database.execute(ASSIGN_DUTY_SQL, idAccount, idDuty);
	}
	
	/** Gets the unassigned duties */
	public Duty[] getUnassigned() throws SQLException {
		return database.buildArray(Duty.class, GET_UNASSIGNED_SQL);
	}
	
	/** Gets the duties for a given event */
	public Duty[] getForEvent(int idEvent) throws SQLException {
		return database.buildArray(Duty.class, GET_FOR_EVENT_SQL, idEvent);
	}
	
	/** Deletes the given duty */
	public void delete(int idDuty) throws SQLException {
		database.execute(DELETE_DUTY_SQL, idDuty);
	}
}
