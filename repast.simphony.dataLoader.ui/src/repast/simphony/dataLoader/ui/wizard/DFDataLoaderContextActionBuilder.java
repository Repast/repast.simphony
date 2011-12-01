package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.engine.DFDataLoaderControllerAction;
import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.engine.DelimitedFileContextBuilder;
import repast.simphony.freezedry.datasource.DelimitedFileDataSource;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 */
public class DFDataLoaderContextActionBuilder extends FreezeDryerContextActionBuilder {

	protected String directoryName;

	protected char delimiter = DelimitedFileDataSource.DEFAULT_DELIMITER;
	
	public DFDataLoaderContextActionBuilder(DelimitedFileContextBuilder loader) {
		super(loader);
		if (loader != null) {
			this.directoryName = loader.getPathName();
			this.delimiter = loader.getDelimiter();
		}
	}

	public String getDirectoryName() {
		return directoryName;
	}

	public void setDirectoryName(String directoryName) {
		this.directoryName = directoryName;
	}

	public void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}
	
	public char getDelimiter() {
		return delimiter;
	}
	
	@Override
	protected DataLoaderControllerAction createAction(Scenario scenario, Object parentId) {
		return new DFDataLoaderControllerAction("Delimited File Data Loader",
				new DelimitedFileContextBuilder(createContextFromData, classesToLoad, freezeDryedContextId,
						directoryName, delimiter), scenario);
	}
}
