/*CopyrightHere*/
package repast.simphony.freezedry.datasource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import saf.core.ui.util.UIUtilities;
import simphony.util.messages.MessageCenter;

import javax.sql.DataSource;
import javax.swing.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility methods for connecting to and working with databases.
 * 
 * @author Jerry Vos
 */
public class DBUtils {
	private static final MessageCenter LOG = MessageCenter.getMessageCenter(DBUtils.class);

	/**
	 * Gets a data source pointing to a database. The data source is a {@link PoolingDataSource}
	 * with a connection pool that will automatically grow when all of its members are in use and
	 * another is requested.
	 * 
	 * @param connectionString
	 *            the connection string for the database (its URL)
	 * @param userName
	 *            the username (if necessary)
	 * @param password
	 *            the username (if necessary)
	 * @param driver
	 *            the driver to be used for connecting to the database
	 * @return a {@link PoolingDataSource}
	 * @throws ClassNotFoundException
	 *             when the driver can't be loaded
	 */
	public static DataSource getDataSource(DBConnectionInfo info) throws ClassNotFoundException {
		if (info.driverName != null) {
			Class.forName(info.driverName);
		}

		GenericObjectPool connectionPool = new GenericObjectPool(null);
		connectionPool.setWhenExhaustedAction(GenericObjectPool.WHEN_EXHAUSTED_GROW);
		ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(info.dbURL,
				info.userName, info.password);
		@SuppressWarnings("unused")
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, true);
		PoolingDataSource dataSource = new PoolingDataSource(connectionPool);

		return dataSource;
	}

	/**
	 * Checks if the table of the specified name exists.
	 * 
	 * @param con
	 *            the connection to the database
	 * @param tableName
	 *            the name of the table
	 * @return if the table exists or not
	 * @throws SQLException
	 *             when there is a problem querying the database
	 */
	public static boolean doesTableExist(Connection con, String tableName) throws SQLException {
		DatabaseMetaData dbm = con.getMetaData();

		ResultSet tables = dbm.getTables(null, null, tableName, null);
		boolean exists = tables.next();

		tables.close();
		if (exists) {
			return true;
		}

		if (tableName.startsWith("\"") && tableName.endsWith("\"") && tableName.length() > 1) {
			tableName = tableName.substring(1, tableName.length() - 1);

			tables = dbm.getTables(null, null, tableName, null);

			exists = tables.next();
		}

		// TODO: potentially check tableName.upperCase()

		return exists;
	}

	/**
	 * Retrieves the names of the columns of the specified table.
	 * 
	 * @param con
	 *            the connection to the database
	 * @param tableName
	 *            the name of the database's table
	 * @return a list of column names
	 * @throws SQLException
	 */
	public static List<String> getColumnNames(Connection con, String tableName) throws SQLException {
		ResultSet set = con.getMetaData().getColumns(null, null, tableName, "%");

		List<String> cols = new ArrayList<String>();
		while (set.next()) {
			cols.add(set.getString("COLUMN_NAME"));
		}

		if (cols.size() == 0 && tableName.startsWith("\"") && tableName.endsWith("\"")
				&& tableName.length() > 1) {
			tableName = tableName.substring(1, tableName.length() - 1);

			set = con.getMetaData().getColumns(null, null, tableName, "%");

			while (set.next()) {
				cols.add(set.getString("COLUMN_NAME"));
			}
		}

		return cols;
	}

	public static boolean validateConnectionSettings(DBConnectionInfo info, String infoMessage/*, String errorMessage*/) {
		while (true) {
			try {
				// try and get a connection
				DataSource dataSource = DBUtils.getDataSource(info);

				// make sure we can connect
				dataSource.getConnection();

				return true;
			} catch (ClassNotFoundException e) {
				LOG.warn("Error loading driver '" + info.driverName + "'.", e);

				JDBCConnectDialog dialog = new JDBCConnectDialog((JFrame) null);
				JDBCConnectPanel panel = dialog.getPanel();

				panel.setUserName(info.userName);
				panel.setDriverName(info.driverName);
				panel.setPassword(info.password);
				panel.setURL(info.dbURL);

				panel.setInfoMessage(infoMessage);
				panel.setErrorMessage(JDBCConnectPanel.DEFAULT_DRIVER_MSG);

				dialog.pack();
				UIUtilities.centerWindowOnScreen(dialog);
				dialog.setModal(true);
				dialog.setVisible(true);

				if (dialog.wasCanceled()) {
					return false;
				}

				info.dbURL = panel.getURL();
				info.driverName = panel.getDriverName();
				info.userName = panel.getUsername();
				info.password = panel.getPassword();
			} catch (SQLException e) {
				LOG.warn(
						"Error connecting to data base driver='" + info.driverName + "', url='" + info.dbURL
								+ "', using password='"
								+ (info.password != null && !info.password.equals("")) + "'.", e);

				JDBCConnectDialog dialog = new JDBCConnectDialog((JFrame) null);
				JDBCConnectPanel panel = dialog.getPanel();

				panel.setUserName(info.userName);
				panel.setDriverName(info.driverName);
				panel.setPassword(info.password);
				panel.setURL(info.dbURL);

				panel.setInfoMessage(infoMessage);
				panel.setErrorMessage(JDBCConnectPanel.DEFAULT_CONNECTING_MSG);

				dialog.pack();
				UIUtilities.centerWindowOnScreen(dialog);
				dialog.setModal(true);
				dialog.setVisible(true);

				if (dialog.wasCanceled()) {
					return false;
				}

				info.dbURL = panel.getURL();
				info.driverName = panel.getDriverName();
				info.userName = panel.getUsername();
				info.password = panel.getPassword();
			}
		}
	}

	public static DataSource getDataSource(String userName, String password, String dbURL, String driverName) throws ClassNotFoundException {
		return getDataSource(new DBConnectionInfo(userName, password, dbURL, driverName));
	}
}
