package repast.simphony.context;

/**
 * A basic concrete implementation of the <code>ContextFactory</code> interface.
 *
 */
public class DefaultContextFactory<T> implements ContextFactory<T> {

	protected DefaultContextFactory() {

	}

	public Context<T> createContext(Object contextId) {
		return new DefaultContext(contextId);
	}

}
