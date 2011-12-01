package repast.simphony.dataLoader.engine;

import repast.simphony.scenario.Scenario;

/**
 * A simple marker class for an action that adds a {@link DelimitedFileContextBuilder}.
 * 
 * @author Jerry Vos
 */
public class DFDataLoaderControllerAction extends
		DataLoaderControllerAction<DelimitedFileContextBuilder> {

	public DFDataLoaderControllerAction(String title, DelimitedFileContextBuilder loader, Scenario scenario) {
		super(title, loader, scenario.getContext());
	}

}
