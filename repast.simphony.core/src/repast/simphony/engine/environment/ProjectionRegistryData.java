package repast.simphony.engine.environment;

import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.projection.Projection;
import repast.simphony.xml.AbstractConverter;

public interface ProjectionRegistryData {

	/**
	 * The projection type Name, e.g. "grid", "network", etc
	 * 
	 * @return
	 */
	public String getTypeName();
	  
	public Class<?> getInterface();
	
	/**
	 * Return true if the attribute is a special projection attribue.
	 * 
	 * @param type
	 * @param attributeId
	 * @return
	 */
	public boolean isProjectionAttribute(String type, String attributeId);
	
	public ProjectionDryer<? extends Projection<?>> getProjectionDryer();

	public void setProjectionDryer(ProjectionDryer<? extends Projection<?>> projectionDryer);

	public AbstractConverter getProjectionXMLConverter();
	
	public void setProjectionXMLConverter(AbstractConverter projectionXMLConverter);
	
}
