
package repast.simphony.jasperReports;

import net.sf.jasperreports.engine.JRDataSource;
import repast.simphony.data2.FileDataSink;

public interface DataSourceFinder {
	JRDataSource getDataSource(FileDataSink outputter);
}