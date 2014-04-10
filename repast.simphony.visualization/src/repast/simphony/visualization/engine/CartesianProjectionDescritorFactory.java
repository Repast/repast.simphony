package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.continuous.ContinuousProjectionDescriptor;
import repast.simphony.visualization.grid.GridProjectionDescriptor;

/**
 * ProjectionDescritorFactory for Cartesian and network ProjectionData
 * 
 * @author Eric Tatara
 *
 */
public class CartesianProjectionDescritorFactory implements ProjectionDescriptorFactory {

	@Override
	public ProjectionDescriptor createDescriptor(ProjectionData proj) {
		if (proj.getType().equals(ProjectionData.GRID_TYPE)) {
			return new GridProjectionDescriptor(proj);
		} 
		
		else if (proj.getType().equals(ProjectionData.CONTINUOUS_SPACE_TYPE)) {
			return new ContinuousProjectionDescriptor(proj);
		}
		
		else {
			return new DefaultProjectionDescriptor(proj);
		}
	}
}
