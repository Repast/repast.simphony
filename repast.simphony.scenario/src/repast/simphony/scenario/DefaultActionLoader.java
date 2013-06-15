package repast.simphony.scenario;

import java.io.File;

import repast.simphony.engine.environment.ControllerAction;

import com.thoughtworks.xstream.XStream;

public class DefaultActionLoader<T extends ControllerAction> extends ObjectActionLoader<T> {
	
	public DefaultActionLoader(File file, Object contextID, Class<T> actionClass, String actionRoot) {
		super(file, contextID, actionClass, actionRoot);
	}

	/**
	 * Override this method if you need to prepare the XStream before using it.
	 * 
	 * @param xstream
	 *            the XStream that will be used for reading in the descriptor
	 */
	protected void prepare(XStream xstream) {
		// do nothing
	}
	
	@Override
	protected ControllerAction createAction(T data, Scenario scenario) {
		return data;
	}
	
	protected ClassLoader getClassLoader() {
		return dataClass.getClassLoader();
	}
}
