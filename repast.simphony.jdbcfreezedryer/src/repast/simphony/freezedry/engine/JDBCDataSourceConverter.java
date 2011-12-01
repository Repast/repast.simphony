/*CopyrightHere*/
package repast.simphony.freezedry.engine;

import repast.simphony.freezedry.datasource.JDBCDataSource;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class JDBCDataSourceConverter implements Converter {
	public boolean canConvert(Class clazz) {
		return JDBCDataSource.class.isAssignableFrom(clazz);
	}
	
	public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
		JDBCDataSource dataSource = (JDBCDataSource) o;
		writer.startNode("dbURL");
		writer.setValue(dataSource.getDBURL());
		writer.endNode();
		writer.startNode("driverName");
		writer.setValue(dataSource.getDriverName());
		writer.endNode();
		if (dataSource.getStoreLogin()) {
			writer.startNode("username");
			writer.setValue(dataSource.getUserName());
			writer.endNode();
			writer.startNode("password");
			writer.setValue(dataSource.getPassword());
			writer.endNode();
		}
	}
	
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		reader.moveDown();
		String dbURL = reader.getValue();
		reader.moveUp();
		reader.moveDown();
		String driverName = reader.getValue();
		reader.moveUp();
		String password = null;
		String userName = null;
		if (reader.hasMoreChildren()) {
			reader.moveDown();
			userName = reader.getValue();
			reader.moveUp();
			reader.moveDown();
			password = reader.getValue();
			reader.moveUp();
		}
		JDBCDataSource dataSource = new JDBCDataSource(dbURL, driverName, userName, password);
		if (userName != null) {
			dataSource.setStoreLogin(true);
		} else {
			dataSource.setStoreLogin(false);
		}
		return dataSource;
	}
}