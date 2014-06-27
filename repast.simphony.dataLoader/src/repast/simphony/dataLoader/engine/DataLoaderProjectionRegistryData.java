package repast.simphony.dataLoader.engine;

/**
 * Interface for projection registry data implementations that provide data 
 *   loading capabilities via the context builder mechanism.
 * 
 * @author Eric Tatara
 *
 */
public interface DataLoaderProjectionRegistryData {

	/**
	 * Provide a projection builder factory that can be used by the context data 
	 *   loader to create the projection type directly from the context XML file.
	 * 
	 * @return the projection builder factory.
	 */
	public ProjectionBuilderFactory getProjectionBuilderFactory();
	
}
