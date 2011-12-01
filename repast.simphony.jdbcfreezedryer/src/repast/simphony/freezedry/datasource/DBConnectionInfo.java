/**
 * 
 */
package repast.simphony.freezedry.datasource;

public class DBConnectionInfo {
	public String userName;
	public String password;
	
	public String dbURL;
	public String driverName;
	
	public DBConnectionInfo(String userName, String password, String dburl, String driverName) {
		super();
		this.userName = userName;
		this.password = password;
		this.dbURL = dburl;
		this.driverName = driverName;
	}
	
	public DBConnectionInfo() {
		
	}
}