package repast.simphony.dataLoader.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.freezedry.engine.JDBCDataSourceConverter;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.DefaultActionLoader;
import repast.simphony.scenario.*;
import repast.simphony.scenario.DefaultControllerActionIO;
import repast.simphony.scenario.Scenario;

import com.thoughtworks.xstream.XStream;

/**
 * A ControllerActionIO for loading / saving JDBCDataLoaderControllerAction-s.
 * 
 * @author Nick Collier
 */
public class JDBCDataLoaderControllerActionIO extends
        DefaultControllerActionIO<JDBCDataLoaderControllerAction> {

	public JDBCDataLoaderControllerActionIO() {
		super(JDBCDataLoaderControllerAction.class, ControllerActionConstants.DATA_LOADER_ROOT);
	}

	@Override
	public ActionSaver getActionSaver() {
		return new DefaultActionSaver<JDBCDataLoaderControllerAction>() {
			@Override
			public void save(XStream xstream, JDBCDataLoaderControllerAction action, String filename)
					throws IOException {
				xstream.registerConverter(new JDBCDataSourceConverter());
				super.save(xstream, action, filename);
			}
		};
	}

	@Override
	public ActionLoader getActionLoader(File actionFile, Object contextID) {
		return new DefaultActionLoader<JDBCDataLoaderControllerAction>(actionFile, contextID,
				JDBCDataLoaderControllerAction.class, ControllerActionConstants.DATA_LOADER_ROOT) {
			@Override
			public void loadAction(XStream xstream, Scenario scenario, ControllerRegistry registry)
					throws FileNotFoundException {
				xstream.registerConverter(new JDBCDataSourceConverter());
				super.loadAction(xstream, scenario, registry);
			}
		};
	}
}
