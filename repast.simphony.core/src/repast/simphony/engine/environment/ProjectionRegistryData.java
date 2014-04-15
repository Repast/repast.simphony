package repast.simphony.engine.environment;

import java.util.List;

import repast.simphony.freezedry.FreezeDryer;
import repast.simphony.freezedry.freezedryers.proj.ProjectionDryer;
import repast.simphony.space.projection.Projection;
import repast.simphony.xml.AbstractConverter;

/**
 * Projection registry data stores information about the available projection
 *   types and their associated capabilities, such as converters, loaders, and
 *   GUI panels.  User-defined projections should implement projection registry
 *   data to provide these basic capabilities to the runtime.
 * 
 * @author Eric Tatara
 *
 * @param <T> The Projection type for this registry data.
 */
public interface ProjectionRegistryData<T extends Projection<?>> {

	/**
	 * The projection type Name, e.g. "grid", "network", etc
	 * 
	 * @return the projection type name.
	 */
	public String getTypeName();
	  
	/**
	 * The projection class that this registry data represents.
	 * 
	 * @return the projection class.
	 */
	public Class<?> getInterface();
	
	/**
	 * Return true if the attribute is a special projection attribute.
	 *  
	 * @param attributeId the name of the attribute.
	 * 
	 * @return
	 */
	public boolean isProjectionAttribute(String attributeId);
	
	/**
	 * Provides an optional projection drier.
	 * 
	 * @return the projection drier.
	 */
	public ProjectionDryer<T> getProjectionDryer();
	
	public List<FreezeDryer<?>> getFreezeDryers();
	
	/**
	 * Provides an optional converter for XML (de-)serialization. 
	 * 
	 * @return the XML converter.
	 */
	public AbstractConverter getProjectionXMLConverter();
	
}