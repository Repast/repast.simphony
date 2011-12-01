/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import repast.simphony.dataLoader.engine.FreezeDryedContextBuilder;
import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;
import simphony.util.messages.MessageCenter;

public class JDBCContextBuilder extends FreezeDryedContextBuilder {

	public static final String SETTINGS_INFO = "<HTML>Note - settings for the data loader action " +
			"will be updated, but not saved unless you do so.";

	@SuppressWarnings("unused")
	private static final MessageCenter LOG = MessageCenter.getMessageCenter(JDBCContextBuilder.class);

	private DBConnectionInfo info;
	
	private boolean storeLogin;

	public JDBCContextBuilder(String dbURL, String driverName, String userName, String password,
	                          boolean storeLogin, boolean createContextFromData, Iterable<Class<?>> classesToLoad,
	                          Object contextId) {
		super(createContextFromData, classesToLoad, contextId);

		this.info = new DBConnectionInfo(userName, password, dbURL, driverName);
		this.storeLogin = storeLogin;
	}

	protected boolean validateConnectionSettings() {
		return DBUtils.validateConnectionSettings(info, SETTINGS_INFO);
	}
	
	@Override
	protected void registerWriters(FreezeDryedRegistry registry) throws FreezeDryingException {
		if (!validateConnectionSettings()) {
			throw new FreezeDryingException(
					"Invalid JDBC connection settings, could not get connection.");
		}
		JDBCDataSource dataSource = new JDBCDataSource(info, storeLogin);
		registry.setDataSource(dataSource);
	}

	public final String getDbURL() {
		return info.dbURL;
	}

	public final String getDriverName() {
		return info.driverName;
	}

	public final String getPassword() {
		return info.password;
	}

	public final String getUserName() {
		return info.userName;
	}
	
	public boolean getStoreLogin() {
		return storeLogin;
	}
	
	public void setStoreLogin(boolean storeLogin) {
		this.storeLogin = storeLogin;
	}

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		JDBCContextBuilder loader = new JDBCContextBuilder("jdbc:mysql://laocalhost/test",
				"com.mysql.jdbc.Driver", "vos", "password", false, false, null, null);
		// JDBCDataLoader loader = new JDBCDataLoader("jdbc:hsqldb:mem:aname",
		// "org.hsqldb.jdbcDriver", "sa", "", false, null, null);

		// JDBCDataSource source = new JDBCDataSource("jdbc:hsqldb:mem:aname",
		// "org.hsqldb.jdbcDriver", "sa", "");

		loader.validateConnectionSettings();
	}
}
