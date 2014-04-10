package repast.simphony.gis.visualization.engine;

import repast.simphony.gis.engine.GeographyProjectionRegistryData;
import repast.simphony.scenario.data.ProjectionData;
import repast.simphony.visualization.engine.ProjectionDescriptor;
import repast.simphony.visualization.engine.ProjectionDescriptorFactory;
import repast.simphony.visualization.gis.GisProjectionDescriptor;
import simphony.util.messages.MessageCenter;

/**
 * ProjectionDescritorFactory for Geography ProjectionData
 * 
 * @author Eric Tatara
 *
 */
public class GISProjectionDescritorFactory implements ProjectionDescriptorFactory {
	 
	private static final MessageCenter msg = MessageCenter.getMessageCenter(GISProjectionDescritorFactory.class);
	 
	@Override
	public ProjectionDescriptor createDescriptor(ProjectionData proj) {
		if (proj.getType().equals(GeographyProjectionRegistryData.NAME)) {
			return new GisProjectionDescriptor(proj);
		} 
		
		else{
			msg.error("Could not find a GIS projection descriptor for: " + proj.getType(), null);
			
			return null;
		}
	}
}