package repast.simphony.visualization.gis;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.AbstractProjectionDescriptor;

/**
 * Projection descriptor for grids.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class GisProjectionDescriptor extends AbstractProjectionDescriptor {

  public GisProjectionDescriptor(ProjectionData proj) {
    super(proj);
  }

  // TODO Projections: Refactor out the implied layoud stuff.
  
  /**
   * Gets the implied 3d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout3D() {
    return null;
  }

  /**
   * Gets the implied 2d layout of the projection, if any.
   * 
   * @return the implied layout of the projection, if any.
   */
  public String getImpliedLayout2D() {
    return null;
  }

}
