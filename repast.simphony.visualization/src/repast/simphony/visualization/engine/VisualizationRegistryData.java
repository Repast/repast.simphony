package repast.simphony.visualization.engine;


/**
 * Visualization registry data stores information about the available visualization
 *   types (displays) and their associated capabilities, such as converters, loaders, and
 *   GUI panels.  User-defined visualizations should implement registry
 *   data to provide these basic capabilities to the runtime.
 * 
 * @author Eric Tatara
 *
 * @param <T> 
 */
public interface VisualizationRegistryData {

	// TODO Projections: store the projections this viz can use.
	
	/**
	 * The visualization type Name, e.g. "2D", "3D", etc
	 * 
	 * @return the projection type name.
	 */
	public String getVisualizationType();
	
	/**
	 * Provide a DisplayCreator that will create the display.
	 * 
	 * @return the projection builder factory.
	 */
	public DisplayCreatorFactory getDisplayCreatorFactory();
	
}