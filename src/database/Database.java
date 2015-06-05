/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Contains generic code for accessing a the database
 */
class Database implements AutoCloseable {
	private final Connection connection;
	
	/** 
	 * Creates a new database connection 
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public Database() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Properties properties = new Properties();
		properties.put("user", "fratsite");
		properties.put("password", "jeff");
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/fratdata", properties);
	}
	
	/** Executes the given sql */
	public void execute(String sql, Object... args) throws SQLException {
		Statement statement = connection.createStatement();
		statement.executeUpdate(String.format(sql, args));
		statement.close();
	}
	
	/**
	 * Executes the given sql and processes the results
	 * 
	 * @param processor the thing that processes the results
	 * @param init the initial value to feed into the processor
	 * @param sql the sql to execute
	 * @param args the supporting arguments of the sql
	 * @return the result of processing the rows
	 * @throws SQLException
	 */
	public <T> T execute(RowProcessor<T> processor, T init, String sql, Object... args) throws SQLException {
		Statement statement = connection.createStatement();
		ResultSet results = statement.executeQuery(String.format(sql, args));
		Row row = new Row(results);
		
		T acc = init;
		while (results.next() && !row.isDone()) {
			acc = processor.process(row, acc);
		}
		
		statement.close();
		
		return acc;
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		connection.close();
	}
}
