package repast.simphony.context;

/**
 * 
 * Interface defines the capability to create <code>Context</code> objects.
 *
 * @param <T>
 */
public interface ContextFactory<T> {

	/**
	 * Creates a new <code>Context</code> with the given identifier. 
	 * @param contextId the identifier to associate with the created <code>Context</code> object.
	 * @return a new <code>Context</code> object.
	 */
	public Context<T> createContext(Object contextId);

}
