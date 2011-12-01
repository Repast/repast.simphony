package repast.simphony.dataLoader.ui;

import repast.simphony.dataLoader.engine.DFDataLoaderControllerAction;
import repast.simphony.dataLoader.ui.wizard.DelimitedFileDataLoaderOption;

/**
 * @author Nick Collier
 */
public class DFDataLoaderActionUI extends DataLoaderActionUI<DFDataLoaderControllerAction> {

	public DFDataLoaderActionUI(DFDataLoaderControllerAction action) {
		super(action, new DelimitedFileDataLoaderOption(), "Delimited File Data Loader");
	}
}
