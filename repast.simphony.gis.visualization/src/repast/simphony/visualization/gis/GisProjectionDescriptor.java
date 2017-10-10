package repast.simphony.visualization.gis;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.gis.visualization.engine.GISDisplayDescriptor;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.AbstractProjectionDescriptor;
import repast.simphony.visualization.engine.DisplayDescriptor;

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

  // TODO Projections: Refactor out the implied layout stuff.
  
  public String getImpliedLayout3D() {
    return null;
  }

  public String getImpliedLayout2D() {
    return null;
  }

	@Override
	public void registerProjectionData(ProjectionData data, DisplayDescriptor descriptor) {
		
		// Register coverage layers as pseudo-projection
		if (GeographyProjectionRegistryData.COVERAGE_NAME.equalsIgnoreCase(data.getType())) {
			((GISDisplayDescriptor)descriptor).addCoverageLayer(data.getId(), null);
		}

		// Register Geography projections as a regular Projection implementation
		else {
			descriptor.addProjection(data, this);
		}
	}
}
