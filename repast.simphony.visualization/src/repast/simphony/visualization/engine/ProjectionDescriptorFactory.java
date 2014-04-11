package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;

/**
 * ProjectionDescriptorFactory implementations create ProjectionDescriptor that
 *   are valid for specific display types.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public interface ProjectionDescriptorFactory {

	public ProjectionDescriptor createDescriptor(ProjectionData proj);
	
}
