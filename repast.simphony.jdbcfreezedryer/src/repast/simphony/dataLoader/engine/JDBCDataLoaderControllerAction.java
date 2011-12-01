package repast.simphony.dataLoader.engine;

import repast.simphony.freezedry.datasource.JDBCContextBuilder;
import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.engine.DelimitedFileContextBuilder;
import repast.simphony.scenario.Scenario;

/**
 * A simple marker class for an action that adds a {@link DelimitedFileContextBuilder}.
 * 
 * @author Jerry Vos
 */
public class JDBCDataLoaderControllerAction extends
		DataLoaderControllerAction<JDBCContextBuilder> {

	public JDBCDataLoaderControllerAction(String title, JDBCContextBuilder loader, Scenario scenario) {
		super(title, loader, scenario.getContext());
	}

}
