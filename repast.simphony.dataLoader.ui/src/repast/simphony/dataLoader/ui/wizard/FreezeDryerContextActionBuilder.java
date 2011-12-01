/*CopyrightHere*/
package repast.simphony.dataLoader.ui.wizard;

import repast.simphony.dataLoader.engine.DataLoaderControllerAction;
import repast.simphony.dataLoader.engine.FreezeDryedContextBuilder;
import repast.simphony.scenario.Scenario;

public abstract class FreezeDryerContextActionBuilder implements ContextActionBuilder {

	protected boolean createContextFromData;
	protected Iterable<Class<?>> classesToLoad;
	protected Object freezeDryedContextId;

	public FreezeDryerContextActionBuilder(FreezeDryedContextBuilder loader) {
		if (loader != null) {
			this.createContextFromData = loader.isCreateContextFromData();
			this.classesToLoad = loader.getClassesToLoad();
			this.freezeDryedContextId = loader.getFreezeDryedContextId();
		}
	}

	public DataLoaderControllerAction getAction(Scenario scenario, Object parentID) {
		return createAction(scenario, parentID);
	}
	
	protected abstract DataLoaderControllerAction createAction(Scenario scenario, Object parentId);

	public Iterable<Class<?>> getClassesToLoad() {
		return classesToLoad;
	}

	public void setClassesToLoad(Iterable<Class<?>> classesToLoad) {
		this.classesToLoad = classesToLoad;
	}

	public Object getFreezeDryedContextId() {
		return freezeDryedContextId;
	}

	public void setFreezeDryedContextId(Object contextId) {
		this.freezeDryedContextId = contextId;
	}

	public boolean isCreateContextFromData() {
		return createContextFromData;
	}

	public void setCreateContextFromData(boolean createContextFromData) {
		this.createContextFromData = createContextFromData;
	}

}
