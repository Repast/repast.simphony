package repast.simphony.jasperReports;

import java.io.FileNotFoundException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import repast.simphony.data2.FileDataSink;
import repast.simphony.data2.FormatType;

public class DefaultDataSourceFinder implements DataSourceFinder {

  public JRDataSource getDataSource(FileDataSink fileSink) {

    if (fileSink.getFormat() == FormatType.TABULAR) {
      try {
        JRCsvDataSource datasource = new JRCsvDataSource(fileSink.getFile());
        datasource.setUseFirstRowAsHeader(true);

        char delim = fileSink.getFormatter().getDelimiter().toCharArray()[0];

        datasource.setFieldDelimiter(delim);

        return datasource;
      } catch (FileNotFoundException e) {
        // TODO: handle this
        e.printStackTrace();
      }
    }

    // TODO: try and handle more types

    return null;
  }
}
