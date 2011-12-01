package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;

/**
 * Default implementation of a ProjectionDescriptor. This holds properties and
 * projection data, but does not have any implied layouts.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultProjectionDescriptor extends AbstractProjectionDescriptor {

  public DefaultProjectionDescriptor(ProjectionData proj) {
    super(proj);
  }

  /**
   * Gets the implied 2d layout of the projection, if any. For example, a grid
   * projection implies a grid layout. This returns null.
   * 
   * @return always returns null.
   */
  public String getImpliedLayout2D() {
    return null;
  }

  /**
   * Gets the implied 3d layout of the projection, if any. For example, a grid
   * projection implies a grid layout. This returns null.
   * 
   * @return always returns null.
   */
  public String getImpliedLayout3D() {
    return null;
  }
}
