package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.scenario.data.ProjectionType;
import repast.simphony.visualization.continuous.ContinuousProjectionDescriptor;
import repast.simphony.visualization.gis.GisProjectionDescriptor;
import repast.simphony.visualization.grid.GridProjectionDescriptor;

/**
 * Factory for creating ProjectionDescriptors based on ProjectionData.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProjectionDescriptorFactory {

  public static ProjectionDescriptor createDescriptor(ProjectionData proj) {
    if (proj.getType() == ProjectionType.GRID) {
      return new GridProjectionDescriptor(proj);
    } else if (proj.getType() == ProjectionType.CONTINUOUS_SPACE) {
      return new ContinuousProjectionDescriptor(proj);
    } else if (proj.getType() == ProjectionType.GEOGRAPHY) {
      return new GisProjectionDescriptor(proj);
    } else {
      return new DefaultProjectionDescriptor(proj);
    }
  }
}
