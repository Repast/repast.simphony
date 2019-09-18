/*CopyrightHere*/
package repast.simphony.batch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.data2.engine.ConsoleSinkControllerActionIO;
import repast.simphony.data2.engine.DataInitActionCreator;
import repast.simphony.data2.engine.DataSetControllerActionIO;
import repast.simphony.data2.engine.DataSetsActionCreator;
import repast.simphony.data2.engine.FileSinkControllerActionIO;
import repast.simphony.data2.engine.TextSinkActionCreator;
import repast.simphony.dataLoader.engine.CNDataLoaderControllerActionIO;
import repast.simphony.dataLoader.engine.ContextXMLDataLoaderControllerActionIO;
import repast.simphony.dataLoader.engine.DFDataLoaderControllerActionIO;
import repast.simphony.dataLoader.engine.DataLoaderCompositeActionCreator;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.gis.engine.GISCompositeControllerActionCreator;
import repast.simphony.plugin.CompositeControllerActionCreator;
import repast.simphony.plugin.ControllerActionIOExtensions;
import repast.simphony.scenario.ScenarioLoader;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.UserPathData;

/**
 * Loads a scenario file so that it can be run in batch mode.
 * 
 * @author Nick Collier
 */
public class BatchScenarioLoader extends ScenarioLoader {

  protected List<CompositeControllerActionCreator> parentActionCreators = new ArrayList<CompositeControllerActionCreator>();
  protected ControllerActionIOExtensions ioExt = new ControllerActionIOExtensions();

  public BatchScenarioLoader(File scenarioDir) {
    super(scenarioDir, null);
    
    parentActionCreators.add(new DataLoaderCompositeActionCreator());
    parentActionCreators.add(new DataSetsActionCreator());
    parentActionCreators.add(new TextSinkActionCreator());
    parentActionCreators.add(new DataInitActionCreator());

    // TODO Projections: This explicit dependency on GIS can only be removed if
    //       the batch mechanism is changed to load ControllerActions via the
    //       JPF plugin loader.
    parentActionCreators.add(new GISCompositeControllerActionCreator());
    
    ioExt.addControllerActionIO(new CNDataLoaderControllerActionIO());
    ioExt.addControllerActionIO(new DataSetControllerActionIO());
    ioExt.addControllerActionIO(new FileSinkControllerActionIO());
    ioExt.addControllerActionIO(new ConsoleSinkControllerActionIO());
    ioExt.addControllerActionIO(new ContextXMLDataLoaderControllerActionIO());
    ioExt.addControllerActionIO(new DFDataLoaderControllerActionIO());
    // ioExt.addControllerActionIO(new JDBCDataLoaderControllerActionIO());
  }

  @Override
  protected void addParentActions(ContextData contextData, ControllerRegistry registry) {
    String id = contextData.getId();
    for (CompositeControllerActionCreator creator : parentActionCreators) {
      
  		if (creator.isMasterOnly()){
				if (id.equals(registry.getMasterContextId())){
					ControllerAction action = creator.createControllerAction();
					registry.addAction(id, null, action);
					registry.registerAction(id, creator.getID(), action);
				}
				else{
					// do nothing.
				}
			}
			else{
				ControllerAction action = creator.createControllerAction();
				registry.addAction(id, null, action);
				registry.registerAction(id, creator.getID(), action);
			}
    }
    
    for (ContextData child : contextData.subContexts()) {
      addParentActions(child, registry);
    }

  }

  @Override
  protected ControllerActionIOExtensions getIOExts() {
    return ioExt;
  }

  protected BatchScenario createScenario(File scenarioDir, ContextData context,
      UserPathData modelData, Object masterContextId) {
    return new BatchScenario(scenarioDir, context, modelData, masterContextId);
  }
}
