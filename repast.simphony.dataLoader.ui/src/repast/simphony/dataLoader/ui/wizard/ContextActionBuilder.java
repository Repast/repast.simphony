package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.scenario.Scenario;

/**
 * In conjuction with a the gui, builds the action that creates a context.
 *
 * @author Nick Collier
 */
public interface ContextActionBuilder {

	/**
	 * Gets an action that will create a context when run.
	 *
	 * @param scenario
	 * @return an action that will create a context when run.
	 */
	DataLoaderControllerAction getAction(Scenario scenario, Object parentContextID);

//	/**
//	 * Sets the <i>descriptor</i> for this DataLoader. The descriptor
//	 * is any object that contains the data necessary to build the context. Exactly
//	 * what that will be depends on the particular data loader.
//	 *
//	 * @param descriptor the data necessary to build a context
//	 */
//	void setDescriptor(T descriptor);
}
