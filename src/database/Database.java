/**
 * 
 */
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * Contains generic code for accessing a the database
 */
public class Database implements IDatabase {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	/** Takes a date and formats it to a string */
	public static String dateToString(Date date) {
		return dateFormat.format(date);
	}
	
	/** Takes a date string and parses it to a date */
	public static Date stringToDate(String date) throws SQLException {
		if (date == null) return null;
		
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			throw new SQLException(e);
		}
	}
	
	private final Connection connection;
	
	/** 
	 * Creates a new database connection
	 */
	public Database(String user, String password, String database) throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Properties properties = new Properties();
		properties.put("user", user);
		properties.put("password", password);
		
		Class.forName("com.mysql.jdbc.Driver");
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, properties);
	}
	
	/** Executes the given sql */
	void execute(String sql, Object... args) throws SQLException {
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
	 * @throws T
	 * @throws SQLException 
	 */
	<R, T extends Exception> R execute(RowProcessor<R, T> processor, R init, String sql, Object... args) throws T, SQLException {
		Statement statement = connection.createStatement();
		statement.execute(String.format(sql, args), Statement.RETURN_GENERATED_KEYS);
		ResultSet results = statement.getResultSet();
		if (results == null) results = statement.getGeneratedKeys();
		Row row = new Row(results);
		
		R acc = init;
		while (results.next() && !row.isDone()) {
			acc = processor.process(row, acc);
		}
		
		statement.close();
		
		return acc;
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getTokenDAO()
	 */
	@Override
	public TokenDAO getTokenDAO() {
		return new TokenDAO(this);
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getPermissionDAO()
	 */
	@Override
	public PermissionDAO getPermissionDAO() {
		return new PermissionDAO(this);
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getAccountsDAO()
	 */
	@Override
	public AccountsDAO getAccountsDAO() {
		return new AccountsDAO(this);
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getRolesDAO()
	 */
	@Override
	public RolesDAO getRolesDAO() {
		return new RolesDAO(this);
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getEventsDAO()
	 */
	@Override
	public EventsDAO getEventsDAO() {
		return new EventsDAO(this);
	}
	
	/* (non-Javadoc)
	 * @see database.IDatabase#getPagesDAO()
	 */
	@Override
	public PagesDAO getPagesDAO() {
		return new PagesDAO(this);
	}

	/* (non-Javadoc)
	 * @see database.IDatabase#getTodoDAO()
	 */
	@Override
	public TodoDAO getTodoDAO() {
		return new TodoDAO(this);
	}

	/* (non-Javadoc)
	 * @see database.IDatabase#getAccountRequestDAO()
	 */
	@Override
	public AccountRequestDAO getAccountRequestDAO() {
		return new AccountRequestDAO(this);
	}

	/* (non-Javadoc)
	 * @see java.lang.AutoCloseable#close()
	 */
	@Override
	public void close() throws Exception {
		connection.close();
	}
	
}
