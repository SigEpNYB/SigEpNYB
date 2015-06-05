/**
 * 
 */
package database;

import java.sql.SQLException;

/**
 * Processes rows from the database one row at a time
 */
interface RowProcessor<T> {

	/**
	 * Processes a row from the database
	 * 
	 * @param row the row to process
	 * @param t the result from processing the last row
	 * @return the result from processing this row
	 */
	public T process(Row row, T t) throws SQLException;
}
