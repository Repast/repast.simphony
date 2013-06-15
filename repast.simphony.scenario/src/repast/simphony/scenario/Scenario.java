/*CopyrightHere*/
package repast.simphony.scenario;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunEnvironmentBuilder;
import repast.simphony.parameter.ParameterSetter;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.UserPathData;

/**
 * Encapsulates a repast simphony scenario.
 *
 * @author Nick Collier
 */
public class Scenario implements ScenarioChangedListener {
  public static final ControllerAction DEFAULT_MASTER_PARENT = new DefaultControllerAction() {
    @Override
    public String toString() {
      return "User Specified Actions";
    }
  };

  public static final String SCENARIO_FILE_NAME = "scenario.xml";

  protected File file;
  protected ContextData context;
  protected UserPathData modelData;
  protected List<ParameterSetter> paramSweepers;
  protected List<ControllerAction> masterActions;

  protected ControllerRegistry registry;
  protected String modelInitName;
  protected File modelPluginPath;
  protected boolean dirty = false;

  public Scenario(File file, ContextData context, UserPathData modelData) {
    this.context = context;
    this.modelData = modelData;
    this.setScenarioDirectory(file);
    this.masterActions = new ArrayList<ControllerAction>();
    this.paramSweepers = new ArrayList<ParameterSetter>();
  }

  public ContextData getContext() {
    return context;
  }
  
  public UserPathData getModelData() {
    return modelData;
  }

  /**
   * Instantiates and returns the first ModelInitializer found in
   * the user's model classpath. If a ModelInitializer is not found, this will
   * return an empty one.
   *
   * @return the first ModelInitializer found in the user's model classpath.
   *         If a ModelInitializer is not found, this will return an empty one.
   * @throws IOException            on file errors
   * @throws ClassNotFoundException if there some error when creating the classes.
   */
  public ModelInitializer getModelInitializer() throws IOException, ClassNotFoundException, IllegalAccessException,
          InstantiationException {
    if (modelInitName != null && modelInitName.length() > 0) {
      Class<?> clazz = Class.forName(modelInitName, true, getClass().getClassLoader());
      if (!ModelInitializer.class.isAssignableFrom(clazz)) {
        throw new IOException("Class '" + modelInitName + "' does not implement the ModelInitializer interface");
      }
      return (ModelInitializer) clazz.newInstance();
    }

    return new ModelInitializer() {
      public void initialize(Scenario scen, RunEnvironmentBuilder builder) {
      }
    };
  }

  /**
   * Gets the scenario directory.
   *
   * @return the scenario directory.
   */
  public File getScenarioDirectory() {
    return file;
  }

  /**
   * Sets the scenario directory.
   */
  public void setScenarioDirectory(File dir) {
    file = dir;
    ScenarioUtils.setScenarioDir(dir);
  }

  public void addParameterSetter(ParameterSetter paramInit) {
    paramSweepers.add(paramInit);
  }

  public void removeParameterSetter(ParameterSetter paramSetter) {
    paramSweepers.remove(paramSetter);
  }

  public Collection<ParameterSetter> getParameterSetters() {
    return Collections.unmodifiableCollection(paramSweepers);
  }

  /**
   * Adds the specified ControllerAction to the master context.
   *
   * @param action the action to add
   */
  public void addMasterControllerAction(ControllerAction action) {
    masterActions.add(action);
  }

  /**
   * Removes the specified ControllerAction to the master context.
   *
   * @param action the action to remove
   */
  public void removeMasterControllerAction(ControllerAction action) {
    masterActions.add(action);
  }

  /**
   * Gets all the user added master controller actions.
   *
   * @return all the user added master controller actions.
   */
  public Collection<ControllerAction> getMasterControllerActions() {
    return masterActions;
  }

  public void setControllerRegistry(ControllerRegistry registry) {
    this.registry = registry;
  }

  public ControllerRegistry getControllerRegistry() {
    return registry;
  }

  public String getModelInitName() {
    return modelInitName;
  }

  public void setModelInitName(String modelInitName) {
    this.modelInitName = modelInitName;
  }

  public File getModelPluginPath() {
    return modelPluginPath;
  }

  public void setModelPluginPath(File modelPluginPath) {
    this.modelPluginPath = modelPluginPath;
  }
  
  /**
   * Gets whether or not this scenario is dirty, i.e. its current state is
   *  not saved.
   *  
   * @return true if this scenario is dirty, otherwise false.
   */
  public boolean isDirty() {
    return dirty;
  }
  
  /**
   * Set the dirty flag on this scenario.
   */
  public void setDirty(boolean dirty) {
    this.dirty = dirty;
  }
 
  /* (non-Javadoc)
   * @see repast.simphony.scenario.ScenarioChangedListener#scenarioChanged(repast.simphony.scenario.ScenarioChangedEvent)
   */
  @Override
  public void scenarioChanged(ScenarioChangedEvent evt) {
    //System.out.println("Dirty: " + evt.getProperty());
    dirty = true;
  }
}
