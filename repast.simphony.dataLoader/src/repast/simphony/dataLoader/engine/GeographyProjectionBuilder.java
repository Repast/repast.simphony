package repast.simphony.dataLoader.engine;

import repast.simphony.context.Context;
import repast.simphony.context.space.gis.GeographyFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.space.gis.GeographyParameters;

/**
 * Builds a Geography based on SGeography data.
 *
 * @author Nick Collier
 */
public class GeographyProjectionBuilder implements ProjectionBuilderFactory {

  private static class GeographyBuilder implements ContextBuilder {

    private String name;

    GeographyBuilder(ProjectionData proj) {
      this.name = proj.getId();
    }


    public Context build(Context context) {
      GeographyParameters geoParams = new GeographyParameters();
      GeographyFactoryFinder.createGeographyFactory(null).createGeography(name, context, geoParams);

      return context;
    }
  }


  /**
   * Gets a ContextBuilder to build the specified
   * Projection.
   *
   * @param proj the type of Projection to build
   * @return a ContextBuilder to build the specified
   *         Projection.
   */
  public ContextBuilder getBuilder(ProjectionData proj) {
    return new GeographyBuilder(proj);
  }
}