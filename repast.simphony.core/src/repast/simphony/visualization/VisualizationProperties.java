package repast.simphony.visualization;


/**
 * Wraps layout properties. 
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface VisualizationProperties {

	/**
	 * Gets the property associated with the specified key.
	 *
	 * @param key
	 * @return the property associated with the specified key.
	 */
	public Object getProperty(String key);

	/**
	 * Gets an iterable over all the valid keys for these properties.
	 *
	 * @return an iterable over all the keys for these properties.
	 */
	public Iterable<String> getKeys();
}
