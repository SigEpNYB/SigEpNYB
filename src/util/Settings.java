/**
 * 
 */
package util;

/**
 * Manages the settings for the app
 */
public class Settings {
	private static Settings uniqueInstance;
	
	/** Singleton */
	private Settings() { }
	
	/** Gets an instance of settings */
	public static Settings getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Settings();
		}
		return uniqueInstance;
	}
	
	/** Gets the user name for the database */
	public String getDatabaseUser() {
		return "fratsite";
	}
	
	/** Gets the password for the database */
	public String getDatabasePassword() {
		return "jeff";
	}
	
	/** Gets the name of the database to use */
	public String getDatabase() {
		return "fratdata";
	}
	
	/** Checks if the database should be cleaned */
	public boolean shouldCleanDatabase() {
		return false;
	}
}
