package repast.simphony.freezedry.engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.scenario.ActionLoader;
import repast.simphony.scenario.ActionSaver;
import repast.simphony.scenario.DefaultActionLoader;
import repast.simphony.scenario.DefaultActionSaver;
import repast.simphony.scenario.DefaultControllerActionIO;
import repast.simphony.scenario.Scenario;

import com.thoughtworks.xstream.XStream;

/**
 * @author Nick Collier
 */
public class JDBCFreezerActionIO extends DefaultControllerActionIO<JDBCFreezerControllerAction> {
	public JDBCFreezerActionIO() {
		super(JDBCFreezerControllerAction.class, ControllerActionConstants.MISC_ROOT);
	}
	
	@Override
	public ActionSaver getActionSaver() {
		return new DefaultActionSaver<JDBCFreezerControllerAction>() {
			@Override
			public void save(XStream xstream, JDBCFreezerControllerAction action, String filename) throws IOException {
				xstream.registerConverter(new JDBCDataSourceConverter());
				super.save(xstream, action, filename);
			}
		};
	}

	@Override
	public ActionLoader getActionLoader(File actionFile, Object contextID) {
		return new DefaultActionLoader<JDBCFreezerControllerAction>(actionFile, contextID,
				JDBCFreezerControllerAction.class, ControllerActionConstants.MISC_ROOT) {
			@Override
			public void loadAction(XStream xstream, Scenario scenario, ControllerRegistry registry)
					throws FileNotFoundException {
				xstream.registerConverter(new JDBCDataSourceConverter());
				super.loadAction(xstream, scenario, registry);
			}
		};
	}
}
