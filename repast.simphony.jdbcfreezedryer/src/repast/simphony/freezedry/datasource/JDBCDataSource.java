package repast.simphony.freezedry.datasource;


import java.sql.Connection;
import java.sql.DataTruncation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringEscapeUtils;

import repast.simphony.freezedry.AbstractDataSource;
import repast.simphony.freezedry.FieldUtilities;
import repast.simphony.freezedry.FreezeDryedObject;
import repast.simphony.freezedry.FreezeDryingException;
import simphony.util.messages.MessageCenter;

public class JDBCDataSource extends AbstractDataSource<ResultSet> {
	// NOTE: ON MYSQL MUST TURN ON ANSI_QUOTES
	public static final String DEFAULT_CLASS_MAPPING_TABLE = "\"ClassMappings\"";
	
	public static final String CLASS_NAME_COL = "ClassName";
	
	private static final MessageCenter LOG = MessageCenter.getMessageCenter(JDBCDataSource.class);
	
	private DBConnectionInfo info;
	
//	private boolean performBatch = false;

	private transient DataSource dataSource;

	private transient Map<String, Connection> connectionTable;

	private transient Map<String, Connection> childConnectionTable;

	protected transient Map<String, String> classIdMap;


	private transient HashMap<Class, ResultSet> resultSets;
	
	private transient HashMap<Class, ResultSet> childResultSets;

	private transient boolean validated;

	protected boolean storeLogin;
	
	public JDBCDataSource(String connectionString, String driverClassName, String userName, String password) {
		this();
		this.info = new DBConnectionInfo(userName, password, connectionString, driverClassName);
	}

	public JDBCDataSource(DBConnectionInfo info) {
		this(info, false);
	}

	public JDBCDataSource(DBConnectionInfo info, boolean storeLogin) {
		this();
		this.info = info;
		this.storeLogin = storeLogin;
	}
	
	JDBCDataSource() {
		setupMaps();
	}


	@Override
	protected String getValue(String value) {
		return "\'" + StringEscapeUtils.escapeJava(value) + "\'";
		// return "\"" + StringEscapeUtils.escapeJava(value) + "\"";
		// return StringEscapeUtils.escapeJava(value);
	}
	
	protected Connection getConnection() throws SQLException {
		try {
			if (dataSource == null) {
				dataSource = DBUtils.getDataSource(info);
			}
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException("Couldn't load database connection library", ex);
		}
			
		
		return dataSource.getConnection();
	}

	protected Connection getConnection(FreezeDryedObject fdo, boolean canCreate) throws SQLException {
		return getConnection(fdo.getType(), canCreate);
	}

	protected Connection getConnection(Class clazz, boolean canCreate) throws SQLException {
		Connection con = null;
		if (!connectionTable.containsKey(getType(clazz)) && canCreate) {
			con = getConnection();
			connectionTable.put(getType(clazz), con);
		}
		
		return connectionTable.get(getType(clazz));
	}

	/**
	 * This is needed because the fields are marked transient and if we serialize with XStream the constructor isn't called,
	 * so these wouldn't be initialized.
	 */
	private void setupMaps() {
		if (connectionTable == null) {
			connectionTable = new HashMap<String, Connection>();
			childConnectionTable = new HashMap<String, Connection>();
			classIdMap = new HashMap<String, String>();
			resultSets = new HashMap<Class, ResultSet>();
			childResultSets = new HashMap<Class, ResultSet>();
		}
	}

	protected Connection getChildConnection(FreezeDryedObject fdo, boolean canCreate) throws SQLException {
		return getChildConnection(fdo.getType(), canCreate);
	}

	protected Connection getChildConnection(Class clazz, boolean canCreate) throws SQLException {
		Connection con = null;
		if (!childConnectionTable.containsKey(getType(clazz)) && canCreate) {
			con = getConnection();
			childConnectionTable.put(getType(clazz), con);
		}
		
		return childConnectionTable.get(getType(clazz));
	}
	
	
	@Override
	protected void startTypeWrite(FreezeDryedObject fdo) throws FreezeDryingException {
		setupMaps();
		Connection con = null;
		if (!validated) {
			if (!validateConnectionSettings()) {
				throw new FreezeDryingException("Could not connect to data base.");
			}
		}
		try {
			con = getConnection(fdo, true);
			
			createTableIfNeeded(con, fdo);
		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.close();
					connectionTable.remove(getType(fdo));
				} catch (SQLException e) {
				}
			}
			
			throw new FreezeDryingException(ex);
		}
		
	}
	
	@Override
	protected void finishTypeWrite(FreezeDryedObject fdo) throws FreezeDryingException {
		Connection con = null;
		try {
			con = getConnection(fdo, false);
			
			// TODO: possibly do any batch updates
			
			if (con != null) {
				con.close();
			}
		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
				}
			}
			
			throw new FreezeDryingException(ex);
		} finally {
			connectionTable.remove(getType(fdo));
		}
	}

	@Override
	protected void writeRow(FreezeDryedObject fdo, Map<String, Object> row) throws FreezeDryingException {
		List<String> cols = getColumns(fdo);
		StringBuilder builder = new StringBuilder("Insert into ");
		builder.append(getTableName(fdo));
		builder.append("(");
		for (int i = 0; i < cols.size(); i++) {
			builder.append(cleanColName(cols.get(i)));
			if (i < cols.size() - 1) {
				builder.append(", ");
			}
		}
		builder.append(") ");
		
		builder.append("Values(");
		
		for (int i = 0; i < cols.size(); i++) {
			builder.append(row.get(cols.get(i)).toString());
			if (i < cols.size() - 1) {
				builder.append(", ");
			}
		}
		builder.append(")");
		
		try {
			Connection con = getConnection(fdo, false);
			
			executeInsert(con, builder.toString());
		} catch (SQLException ex) {
			if (ex instanceof DataTruncation) {
				LOG.warn("Data truncation warning while writing data '" + row + "'.", ex);
				return;
			} else if (ex instanceof SQLWarning) {
				LOG.warn("Received a sql warning while writing data '" + row + "'.", ex);
			}
			throw new FreezeDryingException(ex);
		}
	}

	
	
	protected String getTableName(String type) {
		if (!type.startsWith("\"") && !type.endsWith("\"")) {
			type = "\"" + type + "\"";
		}
//		return type;
		return type.replaceAll("\\.|\\[|\\(", "_");
	}

	protected String getTableName(FreezeDryedObject fdo) {
		return getTableName(getType(fdo));
	}

	protected String getChildTableName(FreezeDryedObject fdo) {
		String parentName = getTableName(getType(fdo));
		return parentName.substring(0, parentName.length() - 1) + CHILDREN_MARKER + "\"";
	}
	
	protected String cleanColName(String name) {
		return getTableName(name);
	}
	
	protected void createTableIfNeeded(Connection con, FreezeDryedObject fdo) throws SQLException {
		String tableName = getTableName(fdo);
		
		if (!DBUtils.doesTableExist(con, tableName)) {
			// create the table
			List<String[]> keyPairs = getColumnKeysetPairs(fdo);
			
			StringBuilder builder = new StringBuilder("Create table ");
			builder.append(tableName);
			builder.append("(");
			for (int i = 0; i < keyPairs.size(); i++) {
				builder.append(cleanColName(keyPairs.get(i)[1]));
				builder.append(" ");
				builder.append(getColumnType(fdo, keyPairs.get(i)[0]));
				if (i < keyPairs.size() - 1) {
					builder.append(", ");
				}
			}
			builder.append(")");
			
			LOG.info("Creating for fdo(" + fdo.toString() + ")");
			executeCreate(con, builder.toString());
		} else {
			// TODO: check to make sure the table is of the correct form
		}
	}
	
	protected void createChildTableIfNeeded(Connection con, FreezeDryedObject fdo) throws SQLException {
		String tableName = getChildTableName(fdo);
		
		if (!DBUtils.doesTableExist(con, tableName)) {
			// create the table
			StringBuilder builder = new StringBuilder("Create table ");
			builder.append(tableName);
			builder.append('(');
			builder.append(PARENT_ID_COL).append(" VARCHAR(100), ");
			builder.append(CHILD_CLASS_COL).append(" VARCHAR(100), ");
			builder.append(CHILD_ID_COL).append(" VARCHAR(100))");
			
			LOG.info("Creating for fdo(" + fdo.toString() + ")'s children");
			executeCreate(con, builder.toString());
		} else {
			// TODO: check to make sure the table is of the correct form
		}
	}
	
	protected String getColumnType(FreezeDryedObject fdo, String colName) {
		Object value = fdo.get(colName);
		if (colName == ID_COL) {
			return "CHAR(" + getValue(fdo.getId()).length() + ") PRIMARY KEY";
		} else if (colName.equals(getChildrenColumnName(fdo))) {
			return "VARCHAR(1024)";
		} else if (value instanceof String) {
			return "VARCHAR(1024)";
		} else if (value != null && FieldUtilities.INSTANCE.isPrimitiveArray(value.getClass())) {
			return "VARCHAR(1024)";
		} else if (value instanceof Double) {
			return "DOUBLE";
		} else if (value instanceof Float) {
			return "FLOAT";
		} else if (value instanceof Number) {
			return "NUMERIC";
		} else if (value instanceof byte[]) {
			return "BLOB";
		} else if (value instanceof Boolean) {
			return "BOOLEAN";
		}
		LOG.warn("Couldn't determine type for object '" + value + "' of type "
				+ (value != null ? value.getClass() : null) + ".");
		return null;
	}
	
	protected void executeCreate(Connection con, String creationSQL) throws SQLException {
		Statement statement = null;
		try {
			LOG.info("Creating table with string: '" + creationSQL + "'.");
			
			statement = con.createStatement();
			statement.execute(creationSQL);
			statement.close();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
		
	}

	private void executeInsert(Connection con, String insertSQL) throws SQLException {
		Statement statement = null;
		try {
			LOG.info("Updating table with string: '" + insertSQL + "'.");
		
			statement = con.createStatement();
			statement.executeUpdate(insertSQL);
			statement.close();
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	private ResultSet executeQuery(Statement statement, Connection con, String querySQL) throws SQLException {
		try {
			LOG.info("Querying table with string: '" + querySQL + "'.");
		
			ResultSet set = statement.executeQuery(querySQL);
		
			return set;
		} catch (SQLException ex) {
			throw ex;
		}
	}
	
	@Override
	protected void startChildrenWrite(FreezeDryedObject fdo) throws FreezeDryingException {
		Connection con = null;
		try {
			con = getChildConnection(fdo, true);
			createChildTableIfNeeded(con, fdo);
		} catch (SQLException ex) {
			if (con != null) {
				try {
					con.close();
					childConnectionTable.remove(getType(fdo));
				} catch (SQLException e) {
				}
			}
			
			throw new FreezeDryingException(ex);
		}
	}

	@Override
	protected void finishChildrenWrite(FreezeDryedObject fdo) throws FreezeDryingException {
		try {
			Connection con = getChildConnection(fdo, false);
			con.close();
		} catch (SQLException e) {
			throw new FreezeDryingException(e);
		} finally {
			childConnectionTable.remove(getType(fdo));
		}
	}
		
	@Override
	protected String getType(Class clazz) {
		return getType(clazz.getName());
	}
	
	protected String queryDBForType(String classType) throws SQLException {
		String query = "Select " + CLASS_NAME_COL + " from " + DEFAULT_CLASS_MAPPING_TABLE + 
		" where " + ID_COL + " = '" + classType + "'";
		Connection con = null;
		
		try {
			con = getConnection();
			
			if (!DBUtils.doesTableExist(con, DEFAULT_CLASS_MAPPING_TABLE)) {
				return null;
			}
			
			ResultSet set = executeQuery(con.createStatement(), con, query);
			if (set.next()) {
				return set.getString(CLASS_NAME_COL);
			} else {
				return null;
			}
		} catch (SQLException ex) {
			throw ex;
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				// swallow this exception
			}
		}
	}
	
	protected String getType(String classType) {
		if (!classIdMap.containsKey(classType)) {
			try {
				String dbResult = queryDBForType(classType);
				classIdMap.put(classType, dbResult);
				if (dbResult != null) {
					return dbResult;
				}
			} catch (SQLException ex) {
				// just swallow this error and continue
				LOG.warn("Could not query the DB for the type of '" + classType + "'.", ex);
			}
			
			String id = generateId(classType);
			classIdMap.put(classType, id);
			try {
				storeClassMapping(classType, id);
			} catch (SQLException e) {
				LOG.warn("Could not store to the DB the type of '" + classType + "'.", e);
				// swallow this and continue
			}
		}
		return classIdMap.get(classType);
	}

	protected String generateId(Class<?> clazz) {
		return generateId(clazz.getName());
	}

	protected String generateId(String className) {
		// TODO: append this with some sort of random unique number or ...
		return getTableName(className);
	}
	
	protected String getClassMapping(String className) throws SQLException {
		Statement statement = null;
		try {
			Connection con = getConnection();
			
			if (!DBUtils.doesTableExist(con, DEFAULT_CLASS_MAPPING_TABLE)) {
				executeCreate(con, "Create table " + DEFAULT_CLASS_MAPPING_TABLE + "(" + ID_COL
						+ " VARCHAR(100) PRIMARY KEY, " + CLASS_NAME_COL + " VARCHAR(100))");
			}
			
			statement = con.createStatement();
			
			ResultSet set = executeQuery(statement, con, "Select " + CLASS_NAME_COL + " from " + DEFAULT_CLASS_MAPPING_TABLE + 
					" where " + ID_COL + " = '" + className + "'");
			
			if (set.next()) {
				return set.getString(CLASS_NAME_COL);
			}
			
			return null;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (statement != null) {
				statement.close();
			}
		}
	}
	
	protected void storeClassMapping(String clazz, String id) throws SQLException {
		Connection con = null;
		try {
			con = getConnection();
			
			if (getClassMapping(clazz) != null) {
				LOG.warn("Class mapping store was attempted with class '" + clazz + "', however that "
						+ "class already has a mapping. Not storing and continuing.");
				return;
			}
			
			executeInsert(con, "Insert into " + DEFAULT_CLASS_MAPPING_TABLE + "(" + ID_COL
					+ ", " + CLASS_NAME_COL + ") Values('" + clazz + "', '" + id + "')");
			
		} catch (SQLException ex) {
			throw ex;
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	@Override
	protected void writeChildData(FreezeDryedObject fdo, HashMap<String, Object> child) throws FreezeDryingException {
		Connection con = null;
		
		try {
			con = getChildConnection(fdo, false);
			
			StringBuilder builder = new StringBuilder("Insert into ");
			builder.append(getChildTableName(fdo));
			builder.append('(');
			builder.append(PARENT_ID_COL).append(", ");
			builder.append(CHILD_CLASS_COL).append(", ");
			builder.append(CHILD_ID_COL).append(")");
			
			builder.append(" Values('").append(child.get(PARENT_ID_COL).toString());
			builder.append("', '").append(child.get(CHILD_CLASS_COL).toString());
			builder.append("', '").append(child.get(CHILD_ID_COL).toString()).append("')");
			
			executeInsert(con, builder.toString());
		} catch (SQLException ex) {
			throw new FreezeDryingException(ex);
		}
	}
	
	@Override
	protected void startTypeRead(Class clazz) throws FreezeDryingException {
		setupMaps();
		Connection con = null;
		if (!validated) {
			if (!validateConnectionSettings()) {
				throw new FreezeDryingException("Could not connect to data base.");
			}
		}
		try {
			con = getConnection(clazz, true);
		} catch (SQLException ex) {
			if (con != null) {
				connectionTable.remove(getType(clazz));
			}
			throw new FreezeDryingException(ex);
		}
	}

	protected boolean validateConnectionSettings() {
		validated = DBUtils.validateConnectionSettings(info, "");

		return validated;
	}

	@Override
	protected void endTypeRead(Class clazz) {
		Connection con = null;
		try {
			closeQuery(clazz);
			con = getConnection(clazz, false);
			if (con != null) {
				con.close();
			}
		} catch (SQLException ex) {
			LOG.warn("Error closing connection, continuing", ex);
		} finally {
			if (con != null) {
				connectionTable.remove(getType(clazz));
			}
		}
	}

	protected void closeQuery(Class clazz) {
		try {
			if (resultSets.containsKey(clazz)) {
				resultSets.get(clazz).getStatement().close();
			}
		} catch (SQLException ex) {
			LOG.warn("Could not close a query for Class '" + clazz.getName() + "'. Continuing.", ex);
		} finally {
			resultSets.remove(clazz);
		}
	}

	protected void closeChildQuery(Class clazz) {
		try {
			if (childResultSets.containsKey(clazz)) {
				childResultSets.get(clazz).getStatement().close();
			}
		} catch (SQLException ex) {
			LOG.warn("Could not close a child query for Class '" + clazz.getName() + "'. Continuing.", ex);
		} finally {
			childResultSets.remove(clazz);
		}
	}
	
	@Override
	protected ResultSet readRow(Class clazz, String key) throws FreezeDryingException {
		String tableName = getTableName(clazz.getName());
		
		if (!resultSets.containsKey(clazz)) {
			Connection con;
			try {
				con = getConnection(clazz, false);
				String whereString = (key == null ? "" : " where " + ID_COL + "=" + getValue(key));
				
				resultSets.put(clazz, executeQuery(con.createStatement(), con, "Select * from "
						+ tableName + whereString));
			} catch (SQLException e) {
				closeQuery(clazz);
				throw new FreezeDryingException(e);
			}
		}
		
		try {
			ResultSet set = resultSets.get(clazz);
			if (!set.next()) {
				return null;
			}
			return set;
		} catch (SQLException e) {
			closeQuery(clazz);
			throw new FreezeDryingException(e);
		}
	}

	
	@Override
	protected byte[] readByteArray(FreezeDryedObject fdo, String field, Object bytesId) throws FreezeDryingException {
		if (bytesId instanceof byte[]) {
			return (byte[]) bytesId;
		} else {
			LOG.warn("It is expected that a byteArray is just stored in a given column. " +
					"Received an unexpected non-byte[] value.  Returning null");
			return null;
		}
	}

	@Override
	protected Object writeByteArray(FreezeDryedObject object, String fieldName, byte[] bs) throws FreezeDryingException {
		// just return the value so that we can handle this like any other type of value
		// in the DB there is no special reason to handle an array of bytes any different
		// then any other value
		return bs;
	}
	
	@Override
	protected String getArrayType(String field) {
		String arrayType = super.getArrayType(field);
		// if we chopped off the initial [ add it back on
		if (arrayType.charAt(0) != '[') {
			return "[" + arrayType;
		}
		return arrayType;
	}
	
	@Override
	protected void finishChildrenRead(FreezeDryedObject fdo) {
		Connection con = null;
		try {
			closeChildQuery(fdo.getType());
			con = getChildConnection(fdo, false);
			con.close();
		} catch (SQLException ex) {
			LOG.warn("Error closing connection, continuing", ex);
		} finally {
			if (con != null) {
				childConnectionTable.remove(getType(fdo));
			}
		}
	}

	@Override
	protected ResultSet readChildsRow(FreezeDryedObject parent, String id) throws FreezeDryingException {
		String tableName = getChildTableName(parent);
		Class clazz = parent.getType();
		
		if (!childResultSets.containsKey(clazz)) {
			Connection con;
			try {
				con = getChildConnection(clazz, false);
				childResultSets.put(clazz, executeQuery(con.createStatement(), con, "Select * from "
						+ tableName));
			} catch (SQLException e) {
				closeChildQuery(clazz);
				throw new FreezeDryingException(e);
			}
		}
		
		try {
			ResultSet set = childResultSets.get(clazz);
			if (!set.next()) {
				return null;
			}
			return set;
		} catch (SQLException e) {
			closeChildQuery(clazz);
			throw new FreezeDryingException(e);
		}
	}

	@Override
	protected void startChildrenRead(FreezeDryedObject fdo) throws FreezeDryingException {
		Connection con = null;
		try {
			con = getChildConnection(fdo, true);
		} catch (SQLException ex) {
			if (con != null) {
				childConnectionTable.remove(getType(fdo));
			}
			throw new FreezeDryingException(ex);
		}
	}

	@Override
	protected List<String> getFields(String type) throws FreezeDryingException {
		Connection con = null;
		try {
			con = getConnection();
			
			List<String> fields = DBUtils.getColumnNames(con, getTableName(type));
			
			return fields;
		} catch (SQLException ex) {
			throw new FreezeDryingException(ex);
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
			}
		}
	}

	@Override
	protected Object getFieldValue(ResultSet row, String field) throws FreezeDryingException {
		try {
			return row.getObject(field);
		} catch (SQLException e) {
			throw new FreezeDryingException(e);
		}
	}
	

	@Override
	protected String getChildrenColumnName(FreezeDryedObject fdo) {
		String tableName = getChildTableName(fdo);
		return "\"" + SPECIAL_COL_MARKER + tableName.substring(1, tableName.length());
	}

	public String getUserName() {
		return info.userName;
	}
	
	public String getPassword() {
		return info.password;
	}
	
	public String getDBURL() {
		return info.dbURL;
	}
	
	public String getDriverName() {
		return info.driverName;
	}
	
	public boolean getStoreLogin() {
		return storeLogin;
	}
	
	public void setStoreLogin(boolean storeLogin) {
		this.storeLogin = storeLogin;
	}
}
