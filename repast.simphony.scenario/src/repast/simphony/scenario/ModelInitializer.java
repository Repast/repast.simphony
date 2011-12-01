/*CopyrightHere*/
package repast.simphony.scenario;

import repast.simphony.engine.environment.RunEnvironmentBuilder;

/**
 * Interface for classes that want to perform some custom initialization
 * of the runtime context.
 */
public interface ModelInitializer {

  /**
   * Initializes the model context using the specified Scenario and RunEnviromentBuilder.
   * The builder can be used to specify a non-default schedule factory and thus a different
   * type of schedule. The Scenario can be used to add custom controller actions that will
   * get executed when the simulation is setup etc.
   *
   * @param scen the current scenario
   * @param builder the builder used to create parts of the runtime enviroment
   */
  void initialize(Scenario scen, RunEnvironmentBuilder builder);
}
