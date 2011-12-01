package repast.simphony.dataLoader.util;

import repast.simphony.context.Context;

/**
 * DataLoader utilities.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DataLoaderUtilities {

	/**
	 * Depth first search starting with context for context with specified id.
	 * Gets that context.
	 *
	 * @param context the context to start the search with
	 * @param id the id of the context to find
	 * @return the context with the specified id.
	 */
	public Context findContext(Context context, Object id) {
		if (context.getId().equals(id)) return context;
		Iterable<Context> subContexts = context.getSubContexts();
		for (Context child : subContexts) {
			Context target = findContext(child, id);
			if (target != null) return target;
		}
		return null;
	}
}
