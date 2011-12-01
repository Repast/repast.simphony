/*CopyrightHere*/
package repast.simphony.dataLoader.engine;

import repast.simphony.freezedry.FreezeDryedRegistry;
import repast.simphony.freezedry.FreezeDryingException;
import repast.simphony.freezedry.datasource.DelimitedFileDataSource;

public class DelimitedFileContextBuilder extends FreezeDryedContextBuilder {
	private String pathName;
	protected char delimiter = DelimitedFileDataSource.DEFAULT_DELIMITER;
	
	public DelimitedFileContextBuilder(boolean createContextFromData, Iterable<Class<?>> classesToLoad,
	                                   Object contextId, String pathName, char delimiter) {
		super(createContextFromData, classesToLoad, contextId);
		
		this.pathName = pathName;
		this.delimiter = delimiter;
	}

	@Override
	protected void registerWriters(FreezeDryedRegistry registry) throws FreezeDryingException {
    registry.setDataSource(new DelimitedFileDataSource(pathName, delimiter, true));
	}

	public final char getDelimiter() {
		return delimiter;
	}

	public final String getPathName() {
		return pathName;
	}
}
