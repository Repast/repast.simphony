/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import repast.simphony.freezedry.datasource.JDBCDataSource;
import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.freezedry.wizard.DataSourceBuilder;

public class JDBCDSBuilder implements DataSourceBuilder {
	protected String userName;
	
	protected String password;
	
	protected String dbURL;
	
	protected String driverName;

	protected boolean storeLogin;
	
	public JDBCDSBuilder(JDBCDataSource source) {
		if (source != null) {
			this.userName = source.getUserName();
			this.password = source.getPassword();
			this.dbURL = source.getDBURL();
			this.driverName = source.getDriverName();
			this.storeLogin = source.getStoreLogin();
		}
	}

	public FreezeDryedDataSource getDataSource() {
		JDBCDataSource dataSource = new JDBCDataSource(dbURL, driverName, userName, password);
		dataSource.setStoreLogin(storeLogin);
		return dataSource;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driver) {
		this.driverName = driver;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String driverURL) {
		this.dbURL = driverURL;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean getStoreLogin() {
		return storeLogin;
	}
	
	public void setStoreLogin(boolean storeLogin) {
		this.storeLogin = storeLogin;
	}
}