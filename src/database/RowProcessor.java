package database;

/**
 * Processes rows from the database one row at a time
 */
interface RowProcessor<R, T extends Exception> {

	/**
	 * Processes a row from the database
	 * 
	 * @param row the row to process
	 * @param t the result from processing the last row
	 * @return the result from processing this row
	 */
	public R process(Row row, R t) throws T;
}