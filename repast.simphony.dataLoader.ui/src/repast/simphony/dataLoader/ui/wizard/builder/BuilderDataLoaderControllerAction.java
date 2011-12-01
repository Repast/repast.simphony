/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard.builder;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.scenario.Scenario;

public class BuilderDataLoaderControllerAction extends DataLoaderControllerAction<BuilderContextBuilder> {
	public BuilderDataLoaderControllerAction(String title, BuilderContextBuilder loader, Scenario scenario) {
		super(title, loader, scenario.getContext());
	}
}
