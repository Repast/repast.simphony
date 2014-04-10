package repast.simphony.visualization.engine;

import repast.simphony.scenario.data.ProjectionData;

/**
 * Interface for ProjectionDescriptorFactory implementations that create a 
 *   new ProjectionDescriptor from ProjectionData.
 * 
 * @author Nick Collier
 * @author Eric Tatara
 *
 */
public interface ProjectionDescriptorFactory {

	public ProjectionDescriptor createDescriptor(ProjectionData proj);
	
}
