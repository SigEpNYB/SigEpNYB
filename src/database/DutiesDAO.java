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
	private static final String ASSIGN_DUTY_SQL = "UPDATE duties SET idOriginal = IFNULL(idOriginal, %d), idAccount = %d WHERE idDuty = %d";
	private static final String GET_UNASSIGNED_SQL = "SELECT idDuty, idEvent, idType, idAccount FROM duties WHERE idAccount IS NULL";
	private static final String GET_FOR_EVENT_SQL = "SELECT idDuty, idEvent, idType, idAccount FROM duties WHERE idEvent = %d";
	private static final String GET_COUNT_SQL = "SELECT COUNT(*) FROM duties WHERE idOriginal = %d AND idType = %d";
	private static final String DELETE_DUTY_SQL = "DELETE FROM duties WHERE idDuty = %d";
	
	private static final String COUNT = "COUNT(*)";
	
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
		database.execute(ASSIGN_DUTY_SQL, idAccount, idAccount, idDuty);
	}
	
	/** Gets the unassigned duties */
	public Duty[] getUnassigned() throws SQLException {
		return database.buildArray(Duty.class, GET_UNASSIGNED_SQL);
	}
	
	/** Gets the duties for a given event */
	public Duty[] getForEvent(int idEvent) throws SQLException {
		return database.buildArray(Duty.class, GET_FOR_EVENT_SQL, idEvent);
	}
	
	/** Gets the number of duties assigned to the given person of the given type */
	public int getCount(int idAccount, DutyType type) throws SQLException {
		return database.execute((row,  t) -> row.getInt(COUNT), 0, GET_COUNT_SQL, idAccount, type.idType);
	}
	
	/** Deletes the given duty */
	public void delete(int idDuty) throws SQLException {
		database.execute(DELETE_DUTY_SQL, idDuty);
	}
}
