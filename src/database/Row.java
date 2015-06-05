/**
 * 
 */
package database;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * Represents a row of the database
 */
class Row {
	private final ResultSet results;
	private boolean done;
	
	/** Creates a new Row */
	public Row(ResultSet results) {
		this.results = results;
	}

	/** Tells the executor that this is the last row to process */
	public void finish() {
		done = true;
	}
	
	/** Checks if this is the last row to process */
	public boolean isDone() {
		return done;
	}
	
	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(int)
	 */
	public boolean getBoolean(int arg0) throws SQLException {
		return results.getBoolean(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getBoolean(java.lang.String)
	 */
	public boolean getBoolean(String arg0) throws SQLException {
		return results.getBoolean(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(int)
	 */
	public Date getDate(int arg0) throws SQLException {
		return results.getDate(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDate(java.lang.String)
	 */
	public Date getDate(String arg0) throws SQLException {
		return results.getDate(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(int)
	 */
	public double getDouble(int arg0) throws SQLException {
		return results.getDouble(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getDouble(java.lang.String)
	 */
	public double getDouble(String arg0) throws SQLException {
		return results.getDouble(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(int)
	 */
	public int getInt(int arg0) throws SQLException {
		return results.getInt(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getInt(java.lang.String)
	 */
	public int getInt(String arg0) throws SQLException {
		return results.getInt(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(int)
	 */
	public long getLong(int arg0) throws SQLException {
		return results.getLong(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getLong(java.lang.String)
	 */
	public long getLong(String arg0) throws SQLException {
		return results.getLong(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(int)
	 */
	public String getString(int arg0) throws SQLException {
		return results.getString(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getString(java.lang.String)
	 */
	public String getString(String arg0) throws SQLException {
		return results.getString(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(int)
	 */
	public Time getTime(int arg0) throws SQLException {
		return results.getTime(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTime(java.lang.String)
	 */
	public Time getTime(String arg0) throws SQLException {
		return results.getTime(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(int)
	 */
	public Timestamp getTimestamp(int arg0) throws SQLException {
		return results.getTimestamp(arg0);
	}

	/**
	 * @param arg0
	 * @return
	 * @throws SQLException
	 * @see java.sql.ResultSet#getTimestamp(java.lang.String)
	 */
	public Timestamp getTimestamp(String arg0) throws SQLException {
		return results.getTimestamp(arg0);
	}
}
