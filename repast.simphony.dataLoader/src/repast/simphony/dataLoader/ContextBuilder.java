package repast.simphony.dataLoader;

import repast.simphony.context.Context;

/**
 * Interface for classes that build a Context by adding projections,
 * agents and so forth.
 *
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ContextBuilder<T> {

	/**
	 * Builds and returns a context. Building a context consists of filling it with
	 * agents, adding projections and so forth. The returned context does not necessarily
	 * have to be the passed in context.
	 * 
	 * @param context
	 * @return the built context.
	 */
	Context build(Context<T> context);
}
