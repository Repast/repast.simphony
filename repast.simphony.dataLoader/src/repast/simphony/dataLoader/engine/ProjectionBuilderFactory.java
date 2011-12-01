package repast.simphony.dataLoader.engine;

import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.scenario.data.ProjectionData;

/**
 * Interface for classes that create and add
 * projections to contexts.
 *
 * @author Nick Collier
 */
public interface ProjectionBuilderFactory {

  /**
   * Gets a ContextBuilder to build the specified
   * Projection.
   *
   * @param proj the type of Projection to build
   *
   * @return a ContextBuilder to build the specified
   * Projection.
   */
  @SuppressWarnings("unchecked")
  ContextBuilder getBuilder(ProjectionData proj);
}
