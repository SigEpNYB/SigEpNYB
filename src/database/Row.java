/**
 * 
 */
package database;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents a row of the database
 */
class Row {
	private final ResultSet results;
	private boolean done;
	
	/** Creates a new Row */
	Row(ResultSet results) {
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
	
	/** Builds an object of type T from this row */
	public <T> T build(Class<T> type) throws SQLException {
		@SuppressWarnings("unchecked")
		Constructor<T> constructor = (Constructor<T>) type.getConstructors()[0];
		
		Field[] fields = type.getDeclaredFields();
		List<Object> values = new LinkedList<>();
		
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			if (Modifier.isStatic(field.getModifiers())) continue;
			field.setAccessible(true);
			
			Class<?> parameterType = field.getType();
			if (parameterType == int.class) {
				values.add(getInt(field.getName()));
			} else if (parameterType == double.class) {
				values.add(getDouble(field.getName()));
			} else if (parameterType == String.class) {
				values.add(getString(field.getName()));
			} else if (parameterType == Date.class) {
				Timestamp timestamp = getTimestamp(field.getName());
				values.add(timestamp == null ? null : new Date(timestamp.getTime()));
			} else if (parameterType.isEnum()) {
				try {
					Field[] paramFields = parameterType.getDeclaredFields();
					Field idField = null;
					for (Field paramField : paramFields) {
						if (Modifier.isStatic(paramField.getModifiers())) continue;
						idField = paramField;
						break;
					}
					int id = getInt(idField.getName());
					
					for (Object obj : parameterType.getEnumConstants()) {
						if (id == (int) idField.get(obj)) {
							values.add(obj);
							break;
						}
					}
				} catch (SecurityException e) {
					throw new RuntimeException("Enum: " + parameterType + " id field is not accessable");
				} catch (IllegalArgumentException e) {
					throw new RuntimeException("Enum: " + parameterType + " id field is not accessable");
				} catch (IllegalAccessException e) {
					throw new RuntimeException("Enum: " + parameterType + " id field is not accessable");
				}
			} else {
				values.add(build(parameterType));
			}
		}
		
		try {
			return constructor.newInstance(values.toArray());
		} catch (InstantiationException e) {
			throw new RuntimeException("Cannot create object of type " + type, e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Cannot create object of type " + type, e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Cannot create object of type " + type, e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("Cannot create object of type " + type, e);
		}
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
