package repast.simphony.dataLoader.ui.wizard.builder;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.ui.wizard.ContextActionBuilder;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 */
public class GUIContextActionBuilder implements ContextActionBuilder/*<ContextDescriptor>*/ {

	private ContextDescriptor descriptor;

	public DataLoaderControllerAction getAction(Scenario scenario, Object parentContextID) {
		return new BuilderDataLoaderControllerAction("GUI Created Loader", new BuilderContextBuilder(
				descriptor), scenario);
	}
	
	/**
	 * Sets the <i>descriptor</i> for this DataLoader. The descriptor
	 * in this case is a contextDescriptor.
	 *
	 * @param descriptor the data necessary to build a context
	 */
	public void setDescriptor(ContextDescriptor descriptor) {
		this.descriptor = descriptor;
	}
}
