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
    if (proj.getType().equals(ProjectionData.GRID_TYPE)) {
      return new GridProjectionDescriptor(proj);
    } else if (proj.getType().equals(ProjectionData.CONTINUOUS_SPACE_TYPE)) {
      return new ContinuousProjectionDescriptor(proj);
    } else if (proj.getType().equals(ProjectionType.GEOGRAPHY)) {
      return new GisProjectionDescriptor(proj);
    } else {
      return new DefaultProjectionDescriptor(proj);
    }
  }
}
