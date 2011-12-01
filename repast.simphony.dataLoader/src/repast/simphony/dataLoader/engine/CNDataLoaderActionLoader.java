package repast.simphony.dataLoader.engine;

import java.io.File;
import java.io.Reader;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.AbstractActionLoader;
import repast.simphony.scenario.Scenario;
import simphony.util.error.ErrorCenter;
import simphony.util.messages.MessageCenter;

import com.thoughtworks.xstream.XStream;

/**
 * ActionLoader for class name based data loaders.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2006/01/10 17:02:31 $
 */
public class CNDataLoaderActionLoader extends AbstractActionLoader {

	private static final MessageCenter msgCenter = MessageCenter.getMessageCenter(CNDataLoaderActionLoader.class);
	private static final ErrorCenter errCenter = ErrorCenter.getErrorCenter();

	public CNDataLoaderActionLoader(File file, Object contextID) {
		super(file, contextID);
	}

	public void performLoad(Reader reader, XStream xstream, Scenario scenario, ControllerRegistry registry) {
		String className = (String) xstream.fromXML(reader);
		
		try {
			ClassNameDataLoaderAction action = new ClassNameDataLoaderAction(className, scenario);
			ControllerAction parent = registry.findAction(contextID, ControllerActionConstants.DATA_LOADER_ROOT);
			registry.addAction(contextID, parent, action);
		} catch (ClassNotFoundException e) {
			msgCenter.error("On load, unable to create ContextCreator class for '" + className + "'", e);
			errCenter.error(1, "On load, unable to create ContextCreator class for '" + className + "'", e);
		} catch (IllegalAccessException e) {
			msgCenter.error("On load, unable to create ContextCreator class for '" + className + "'", e);
			errCenter.error(1, "On load, unable to create ContextCreator class for '" + className + "'", e);
		} catch (InstantiationException e) {
			msgCenter.error("On load, unable to create ContextCreator class for '" + className + "'", e);
			errCenter.error(1, "On load, unable to create ContextCreator class for '" + className + "'", e);
		}
	}
}
