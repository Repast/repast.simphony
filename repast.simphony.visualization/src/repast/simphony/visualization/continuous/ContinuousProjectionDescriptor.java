package repast.simphony.visualization.continuous;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.AbstractProjectionDescriptor;

/**
 * Projection descriptor for continuous spaces.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ContinuousProjectionDescriptor extends AbstractProjectionDescriptor {

  public ContinuousProjectionDescriptor(ProjectionData proj) {
    super(proj);
  }

  /**
   * Gets the implied 3d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout3D() {
    return Continuous3DLayout.class.getName();
  }

  /**
   * Gets the implied 2d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout2D() {
    return Continuous2DLayout.class.getName();
  }

}
