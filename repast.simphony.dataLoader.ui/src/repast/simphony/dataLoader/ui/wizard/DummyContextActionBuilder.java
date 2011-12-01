package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.scenario.Scenario;

/**
 * Dummy placeholder for gui context creator items that have not been implemented yet.
 *
 * @author Nick Collier
 */
public class DummyContextActionBuilder implements ContextActionBuilder {

	private String label;

	public DummyContextActionBuilder(String label) {
		this.label = label;
	}

	public DataLoaderControllerAction getAction(Scenario spec, Object parentContextID) {
		return null;
	}

	public String getLabel() {
		return label;
	}

	/**
	 * Sets the <i>descriptor</i> for this DataLoader. The descriptor
	 * is any object that contains the data necessary to build the context. Exactly
	 * what that will be depends on the particular data loader.
	 *
	 * @param descriptor the data necessary to build a context
	 */
	public void setDescriptor(Object descriptor) {

	}
}
