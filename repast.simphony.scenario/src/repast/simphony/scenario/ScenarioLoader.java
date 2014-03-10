/*CopyrightHere*/
package repast.simphony.scenario;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import repast.simphony.engine.controller.ControllerActionConstants;
import repast.simphony.engine.controller.DefaultControllerRegistry;
import repast.simphony.engine.controller.ScheduledMethodControllerAction;
import repast.simphony.engine.controller.WatcherControllerAction;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.ProjectionRegistry;
import repast.simphony.engine.environment.ProjectionRegistryData;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.engine.watcher.WatchAnnotationReader;
import repast.simphony.parameter.ParameterConstants;
import repast.simphony.parameter.ParameterFormatException;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.parameter.ParameterType;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.ParametersCreator;
import repast.simphony.parameter.ParametersParser;
import repast.simphony.plugin.ActionExtensions;
import repast.simphony.plugin.ControllerActionIOExtensions;
import repast.simphony.scenario.data.Attribute;
import repast.simphony.scenario.data.AttributeFactory;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.ContextFileReader;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.scenario.data.UserPathFileReader;
import repast.simphony.util.ClassPathEntry;
import simphony.util.messages.MessageCenter;

/**
 * Loads a scenario, setting up the user classpath, action tree, parameters, etc.
 * 
 * @author Nick Collier
 */
public class ScenarioLoader {

  private static MessageCenter msg = MessageCenter.getMessageCenter(ScenarioLoader.class);

  private File scenarioDir;
  private Parameters params;

  private ActionExtensions actionExts;
  protected Scenario scenario;

  public ScenarioLoader(File scenarioDir, ActionExtensions actionExts) {
    this.scenarioDir = scenarioDir;
    this.actionExts = actionExts;
  }

  protected void addContextsToRegistry(ControllerRegistry registry, ContextData parent) {

    for (ContextData child : parent.subContexts()) {
      registry.addContextId(parent.getId(), child.getId());
      addContextsToRegistry(registry, child);
    }
  }

  protected void addParentActions(ContextData context, ControllerRegistry registry) {
    actionExts.getActionExts().addParentControllerActions(context.getId(), registry);
    for (ContextData child : context.subContexts()) {
      addParentActions(child, registry);
    }
  }

  private void addAgentClassNamesToContextData(ContextData context, UserPathData data) {
    try {
      for (ClassPathEntry path : data.agentEntries()) {
        for (String className : path.getClassNames()) {
          addAgentData(className, context);
        }
      }
    } catch (IOException ex) {
      msg.warn("Error while finding agent classes", ex);
    } catch (ClassNotFoundException ex) {
      msg.warn("Error while finding agent classes", ex);
    }
  }

  // adds the agent class name to every context in the context heirarchy
  private void addAgentData(String className, ContextData context) {
    context.addAgent(className);
    for (ContextData data : context.subContexts()) {
      data.addAgent(className);
    }
  }

  /**
   * Gets the parameters that were last loaded.
   * 
   * @return the parameters that were last loaded.
   */
  public Parameters getParameters() {
    return params;
  }

  public ControllerRegistry load(RunEnvironmentBuilder builder) throws ScenarioLoadException {
    ControllerRegistry registry=null;
    try {
      File modelFile = new File(scenarioDir, ScenarioConstants.USER_PATH_FILE_NAME);
      UserPathData data = new UserPathFileReader().read(modelFile);

      // initialize the classpath
      new ScenarioCPInitializer().run(data);
      // initialize / instrument the watchees
      new ScenarioWatcheeInitializer().run(data);

      Classpath contextClasspath = new Classpath();
      for (ClassPathEntry entry : data.classpathEntries()) {
        boolean added = contextClasspath.addEntry(entry);
        if (!added) {
          msg.warn("While initializing classpath: Empty or non-existent path '"
              + entry.getPath().getCanonicalPath() + "'.");
        }
      }

      File contextFile = new File(scenarioDir, ScenarioConstants.CONTEXT_FILE_NAME);
      ContextData rootContext = new ContextFileReader().read(contextFile, contextClasspath);
      registry = new DefaultControllerRegistry();
      registry.setName(data.getName());
      registry.setMasterContextId(rootContext.getId());
      addContextsToRegistry(registry, rootContext);

      Classpath classpath = new Classpath();
      for (ClassPathEntry entry : data.annotationCPEntries()) {
        // check for valid path is not necessary here as it is already
        // done in the above with the context classpath
        classpath.addEntry(entry);
      }
      
      List<Class<?>> annotatedClasses = classpath.getClasses();
      createParameters();
      // add the actions to the registry
      addParentActions(rootContext, registry);

      // get the root action
      ControllerAction scheduleRoot = registry.findAction(rootContext.getId(),
          ControllerActionConstants.SCHEDULE_ROOT);
      // intialize the watchers
      WatchAnnotationReader watchReader = new WatchAnnotationReader();
      watchReader.processClasses(annotatedClasses);
      if (watchReader.watchCount() > 0) {
        ControllerAction watcherAction = new WatcherControllerAction(watchReader);
        registry.addAction(rootContext.getId(), scheduleRoot, watcherAction);
      }

      // setup scheduledMethod annotation reader / executor for master context
      ScheduledMethodControllerAction action = new ScheduledMethodControllerAction(annotatedClasses);
      registry.addAction(rootContext.getId(), scheduleRoot, action);

      addAgentClassNamesToContextData(rootContext, data);

      // this has to occur before the actions are loaded so they get the
      // overridden
      // parameters
      File paramsFile = new File(scenarioDir, "parameters.xml");
      if (paramsFile.exists()) {
        // parse the file and replace the values in the spec file with these
        // ones
        ParametersParser pp = new ParametersParser(params, paramsFile);
        params = pp.getParameters();
      }

      // For backward compatibility with older scenario files, check for and
      // load extended_params.xml if
      // it exists, the file no longer is preserved when a scenario is saved,
      // since the parameters that used
      // to be stored here are now written in the parameters.xml file
      paramsFile = new File(scenarioDir, "extended_params.xml");
      if (paramsFile.exists()) {
        // parse the file and replace the values in the spec file with these
        // ones
        ParametersParser pp = new ParametersParser(params, paramsFile);
        params = pp.getParameters();
      }

      ParametersCreator creator = new ParametersCreator();
      creator.addParameters(params);
      processContextAttributes(creator, rootContext);
      processProjectionAttributes(creator, rootContext);
      params = creator.createParameters();

      scenario = new Scenario(scenarioDir, rootContext, data);
      ScenarioFileLoader loader = new ScenarioFileLoader(getIOExts());
      loader.load(scenarioDir, scenario, registry);
      scenario.setControllerRegistry(registry);

      // check if mode is auto and no data loader
      // if so then create an auto context builder
      // TODO need to put something in place for this
      // doAutoModeCheck(rootContext, registry);

      // run the user's initialization code
      scenario.getModelInitializer().initialize(scenario, builder);

      // add all controller actions that user may have created and that are
      // stored in the scenario
      registry.addAction(registry.getMasterContextId(), null, Scenario.DEFAULT_MASTER_PARENT);
      for (ControllerAction masterAction : scenario.getMasterControllerActions()) {
        registry.addAction(registry.getMasterContextId(), Scenario.DEFAULT_MASTER_PARENT,
            masterAction);
      }

      for (ParameterSetter paramSetter : scenario.getParameterSetters()) {
        registry.addParameterSetter(paramSetter);
      }
    } 
     catch(ClassNotFoundException ex){
    	 msg.warn("Class path not found", ex);
     }
     catch (Exception ex) {
      throw new ScenarioLoadException(ex.getMessage(), ex);
    }
	return registry;
  }
  
  private void processContextAttributes(ParametersCreator creator, ContextData context)
    throws ParameterFormatException {
    for (Attribute attribute : context.attributes()) {
      ParameterType<?> type = AttributeFactory.toParameterType(attribute);
      Object paramVal = type.getValue(attribute.getValue());
      String name = attribute.getId();
      creator.addParameter(name, attribute.getDisplayName(), type.getJavaClass(), paramVal, false);
      creator.addConvertor(name, type.getConverter());
      
    }
  }

  @SuppressWarnings("unchecked")
  /**
   * Note that this must be called after the projection registry is setup.
   * @param creator
   * @param context
   * @throws ParameterFormatException
   */
  private void processProjectionAttributes(ParametersCreator creator, ContextData context)
      throws ParameterFormatException {
  	
    for (ProjectionData proj : context.projections()) {
      
    	for (Attribute attribute : proj.attributes()) {
      	
        if (!isProjectionAttribute(proj.getType(), attribute.getId())) {
        	
          ParameterType type = AttributeFactory.toParameterType(attribute);
          Object paramVal = type.getValue(attribute.getValue());
          String name = proj.getId() + attribute.getId();
          creator.addParameter(name, proj.getId() + " " + attribute.getDisplayName(), type
              .getJavaClass(), paramVal, false);
          creator.addConvertor(name, type.getConverter());
        }
      }
    }

    for (ContextData child : context.subContexts()) {
      processProjectionAttributes(creator, child);
    }
  }
  
  private boolean isProjectionAttribute(String type, String attributeId) {
    
  	// TODO Projections: Use the projection registry for other built in projections.
  	
  	if (type == ProjectionData.NETWORK_TYPE && attributeId.equals("directed")) return false;
    if (type == ProjectionData.GRID_TYPE && attributeId.equals("allows multi")) return false;
    if ((type == ProjectionData.GRID_TYPE || type == ProjectionData.CONTINUOUS_SPACE_TYPE) 
        && attributeId.equals("border rule")) return false;
    
    // The projection registry holds data for additional projections such as GIS.
	  ProjectionRegistryData data = ProjectionRegistry.getDataFor(type);
  	
  	return data.isProjectionAttribute(attributeId);
  }

  /*
   * // check if mode is auto and no data loader // if so then create an auto
   * context builder private void doAutoModeCheck(SContext context,
   * ControllerRegistry registry) { ControllerAction dlParent =
   * registry.findAction(context.getLabel(),
   * ControllerActionConstants.DATA_LOADER_ROOT); if
   * (context.getImplementation()
   * .getMode().equals(SImplementationMode.AUTO_LITERAL) &&
   * registry.getActionTree(context.getLabel()).getChildren(dlParent).size() ==
   * 0) { // we have to do this using ControllerActionIO and the plugin
   * mechanism // because runtime has no dependencies on data loader
   * ControllerActionIO io =getIOExts().getControllerActionIO(
   * "repast.simphony.dataLoader.engine.ScoreDataLoaderAction"); ActionLoader
   * loader = io.getActionLoader(null, context.getLabel()); try {
   * loader.loadAction(null, scenario, registry); } catch (FileNotFoundException
   * e) { // this should never happen because the
   * ScoreDataLoaderControllerActionIO doesn't use a file. e.printStackTrace();
   * } } for (SContext child : context.getSubContexts()) {
   * doAutoModeCheck(child, registry); } }
   */

//  protected Class<?> getClass(SContext context) throws IOException, ClassNotFoundException {
//    ClassLoader loader = ScenarioLoader.class.getClassLoader();
//    SImplementation impl = context.getImplementation();
//    if (impl != null && impl.getClassName().trim().length() > 0) {
//      String path = ContextPersist.platformPath(impl.getDerivedPath() + File.separator
//          + impl.getBinDir());
//      if (!impl.getMode().equals(SImplementationMode.EXTERNAL_LITERAL)) {
//        String qualifiedName = impl.getQualifiedName();
//        if (loader instanceof ExtendablePluginClassLoader) {
//          // the loader will take care of adding the same path more than once
//          // so we don't need to worry about that here
//          ((ExtendablePluginClassLoader) loader).appendPaths(path);
//        }
//        return Class.forName(qualifiedName);
//
//      }
//    }
//    return null;
//  }
//
//  public static List<Class<?>> getAgentClasses(SContext rootContext) {
//    List<Class<?>> clazzes = new ArrayList<Class<?>>();
//    for (SAgent agent : rootContext.getAllAgents()) {
//      if (agent != rootContext) {
//        ClassLoader loader = ScenarioLoader.class.getClassLoader();
//        SImplementation impl = agent.getImplementation();
//        if (impl != null && impl.getClassName().trim().length() > 0) {
//          String path = ContextPersist.platformPath(impl.getDerivedPath() + File.separator
//              + impl.getBinDir());
//
//          if (!impl.getMode().equals(SImplementationMode.EXTERNAL_LITERAL)) {
//            String qualifiedName = impl.getQualifiedName();
//            try {
//              if (loader instanceof ExtendablePluginClassLoader) {
//                // the loader will take care of adding the same path more than
//                // once
//                // so we don't need to worry about that here
//                ((ExtendablePluginClassLoader) loader).appendPaths(path);
//              }
//              clazzes.add(Class.forName(qualifiedName));
//            } catch (IOException e) {
//              throw new InternalError("Unexpected file-system issue");
//            } catch (ClassNotFoundException e) {
//              throw new RuntimeException("Couldn't find classes using supplied class loader: "
//                  + qualifiedName + " on path: " + path);
//            }
//          }
//        }
//      }
//    }
//    return clazzes;
//  }

  private void createParameters() throws IOException, ClassNotFoundException,
      IntrospectionException, ParameterFormatException {
    ParametersCreator creator = new ParametersCreator();
    int val = (int) System.currentTimeMillis();
    // per JIRA 76 - "Use positive default random seeds"
    if (val < 0)
      val = Math.abs(val);

    creator.addParameter(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME,
    // the default value must be null if we want the seed to reset to
        // current time each run
        ParameterConstants.DEFAULT_RANDOM_SEED_DISPLAY_NAME, int.class, null, false);

    this.params = creator.createParameters();
    params.setValue(ParameterConstants.DEFAULT_RANDOM_SEED_USAGE_NAME, val);
  }

  protected ControllerActionIOExtensions getIOExts() {
    return actionExts.getIoExts();
  }

  public Scenario getScenario() {
    return scenario;
  }
}
