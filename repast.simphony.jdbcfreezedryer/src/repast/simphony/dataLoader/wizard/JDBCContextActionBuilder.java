package repast.simphony.dataLoader.wizard;

import repast.simphony.dataLoader.engine.JDBCDataLoaderControllerAction;
import repast.simphony.freezedry.datasource.JDBCContextBuilder;
import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.ui.wizard.FreezeDryerContextActionBuilder;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 */
public class JDBCContextActionBuilder extends FreezeDryerContextActionBuilder {
	protected String userName;
	
	protected String password;
	
	protected String dbURL;
	
	protected String driverName;
	
	protected boolean storeLogin;
	
	public JDBCContextActionBuilder(JDBCContextBuilder loader) {
		super(loader);
		if (loader != null) {
			this.userName = loader.getUserName();
			this.password = loader.getPassword();
			this.dbURL = loader.getDbURL();
			this.driverName = loader.getDriverName();
		}
	}

	@Override
	protected DataLoaderControllerAction createAction(Scenario scenario, Object parentId) {
		return new JDBCDataLoaderControllerAction("JDBC Data Loader",
				new JDBCContextBuilder(dbURL, driverName, userName, password, storeLogin, createContextFromData,
						classesToLoad, freezeDryedContextId), scenario);
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
