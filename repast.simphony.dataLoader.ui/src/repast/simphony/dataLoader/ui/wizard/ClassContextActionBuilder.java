package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.dataLoader.engine.ClassNameContextBuilder;
import repast.simphony.dataLoader.engine.ClassNameDataLoaderAction;
import repast.simphony.scenario.Scenario;

/**
 * @author Nick Collier
 */
public class ClassContextActionBuilder implements ContextActionBuilder {

	private Object loader;

	public ClassNameDataLoaderAction getAction(Scenario scenario, Object parentID) {
		return new ClassNameDataLoaderAction(new ClassNameContextBuilder((ContextBuilder) loader), scenario);
	}

	/**
	 * Sets the DataLoader to be used to load the data.
	 * 
	 * @param loader
	 *            a data loader
	 */
	public void setDataLoader(Object loader) {
		this.loader = loader;
	}
}
