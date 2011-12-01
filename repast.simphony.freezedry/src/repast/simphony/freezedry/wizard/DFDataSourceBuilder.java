/*CopyrightHere*/
package repast.simphony.freezedry.wizard;

import repast.simphony.freezedry.FreezeDryedDataSource;
import repast.simphony.freezedry.datasource.DelimitedFileDataSource;

public class DFDataSourceBuilder implements DataSourceBuilder {
	private String directoryName;

	private char delimiter = DelimitedFileDataSource.DEFAULT_DELIMITER;
  private DelimitedFileDataSource ds;

  public DFDataSourceBuilder(DelimitedFileDataSource source) {
		if (source != null) {
			this.delimiter = source.getDelimiter();
			this.directoryName = source.getPath();
		}
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public String getDirectoryName() {
		return directoryName;
	}
	
	public final char getDelimiter() {
		return delimiter;
	}

	public final void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	public FreezeDryedDataSource getDataSource() {
    if (ds == null) {
      ds = new DelimitedFileDataSource(directoryName, delimiter, false);
    }
    return ds;
  }
}