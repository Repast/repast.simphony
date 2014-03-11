package repast.simphony.visualization.engine;


/**
 * Projection registry data stores information about the available projection
 *   types and their associated capabilities, such as converters, loaders, and
 *   GUI panels.  User-defined projections should implement projection registry
 *   data to provide these basic capabilities to the runtime.
 * 
 * @author Eric Tatara
 *
 * @param <T> 
 */
public interface VisualizationRegistryData {

	// TODO Projections: store the projections this viz can use.
	
	/**
	 * The projection type Name, e.g. "grid", "network", etc
	 * 
	 * @return the projection type name.
	 */
	public String getVisualizationName();
	
	/**
	 * Provide a DisplayCreator that will create the display.
	 * 
	 * @return the projection builder factory.
	 */
	public DisplayCreator getDisplayCreator();
	
	/**
	 * Set the DisplayCreator.
	 * 
	 * @param displayCreator
	 */
	public void setDisplayCreator(DisplayCreator displayCreator);
	
	
}
