/**
 * 
 */
package test;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.runner.RunWith;

import util.Settings;

import com.treetest.junit.ParameterVariables;
import com.treetest.junit.ReturnVariable;
import com.treetest.junit.TreeTestRunner;

import database.Database;
import database.IDatabase;

/**
 * The main testing class
 */
@RunWith(TreeTestRunner.class)
public class Main {
	private static final String TEST_DATABASE_NAME = "test";
	
	@ReturnVariable("database")
	public Database start() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException, IOException {
		Settings settings = Settings.getInstance();
		Database database = new Database(settings.getDatabaseUser(), settings.getDatabasePassword(), settings.getDatabase());
		database.dropSchema(TEST_DATABASE_NAME);
		database.createSchema(TEST_DATABASE_NAME);
		return database;
	}
	
	@ParameterVariables("database")
	public void end(IDatabase database) throws Exception {
		database.dropSchema(TEST_DATABASE_NAME);
		database.close();
	}
}
