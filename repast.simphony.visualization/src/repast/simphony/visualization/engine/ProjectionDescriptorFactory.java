package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.continuous.ContinuousProjectionDescriptor;
import repast.simphony.visualization.grid.GridProjectionDescriptor;

/**
 * Factory for creating ProjectionDescriptors based on ProjectionData.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProjectionDescriptorFactory {

  public static ProjectionDescriptor createDescriptor(ProjectionData proj) {
    if (proj.getType() == ProjectionData.GRID_TYPE) {
      return new GridProjectionDescriptor(proj);
    } else if (proj.getType() == ProjectionData.CONTINUOUS_SPACE_TYPE) {
      return new ContinuousProjectionDescriptor(proj);
    } else if (proj.getType() == ProjectionType.GEOGRAPHY) {
      return new GisProjectionDescriptor(proj);
    } else {
      return new DefaultProjectionDescriptor(proj);
    }
  }
}
