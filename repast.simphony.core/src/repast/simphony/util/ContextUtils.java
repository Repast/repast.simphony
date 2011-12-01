/*CopyrightHere*/
package repast.simphony.util;

import org.apache.commons.collections15.Predicate;
import repast.simphony.context.Context;
import repast.simphony.engine.controller.ContextTraverser;
import repast.simphony.engine.environment.RunState;
import repast.simphony.util.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Utility functions for working with contexts.
 */
public class ContextUtils {
//	private static ArrayList<Context<?>> rootContexts;
//	private static Hashtable<Object, ArrayList<Context<?>>> runIdContextTable;
//	private static Object currentRunId;
//
//	static {
//		rootContexts = new ArrayList<Context<?>>();
//		runIdContextTable = new Hashtable<Object, ArrayList<Context<?>>>();
//	}

	/**
	 * This searches the specified context and its descendants for the context with the specified
	 * id. This searches through the contexts in a breadth-first manner, and will return the first
	 * context whose getId() method returns the specified id. This includes the start context.
	 * 
	 * @param start
	 *            the root context of the search tree (non-null)
	 * @param id
	 *            the id of the context to search for
	 * 
	 * @return the first context found with the given id, or null if no context is found
	 */
	@SuppressWarnings("unchecked")
	public static <T> Context<? extends T> findContext(Context<T> start, final Object id) {
		return CollectionUtils.breadthFirstSearch(new Predicate<Context>() {
			public boolean evaluate(Context context) {
				if (context.getId().equals(id)) {
					return true;
				}
				return false;
			}
		}, new ContextTraverser(), start);
	}
	
	/**
	 * Retrieves the context a given object is in. This will traverse the RunState's current master
	 * context. It then will traverse down the context's sub contexts until it finds the lowest
	 * level context the object is in.
	 * 
	 * <p/>This is the same as
	 * <code>ContextUtils.getContext(RunState.getInstance().getMasterContext(), o)</code>
	 * 
	 * @param o
	 *            an object to find in the contexts
	 * @return the context that contains the object
	 */
	@SuppressWarnings("unchecked")
	public static Context getContext(Object o) {
    if (RunState.getInstance() == null) return null;
    if (RunState.getInstance().getMasterContext() == null) return null;
    return getContext(RunState.getInstance().getMasterContext(), o);
//		Context context=RunState.getInstance().getMasterContext();
//		return context.getSubContext(o);
	}

	/**
	 * Retrieves the context of the specified object starting with the specified Context. This will
	 * traverse into the specified context's sub contexts until it finds the lowest level context
	 * the object is in.
	 * 
	 * @param startingContext
	 *            the context to begin the search at
	 * @param o
	 *            an object to find in the contexts
	 * @return the context that contains the object
	 */
	@SuppressWarnings("unchecked")
	public static Context getContext(Context<?> startingContext, Object o) {
		Context contextFound = null;

		List<Context<?>> list = new ArrayList<Context<?>>();
		list.add(startingContext);
		for (Iterator<Context<?>> iter = list.iterator(); iter.hasNext();) {
			Context context = iter.next();
			if (context.contains(o)) {
				contextFound = context;
				if (context.getSubContexts() != null) {
					iter = context.getSubContexts().iterator();
				} else {
					break;
				}
			}
		}
		// TODO: if null, error handling here
		
		return contextFound;
	}

	/**
	 * Starting with the master context, search for and return the parent context
	 * of the specified context.
	 * @param context
	 * @return the parent context of the specified context, or null
	 * if not found.
	 */
	public static Context getParentContext(Context context) {
		Context master = RunState.getInstance().getMasterContext();
		return getParentContext(master, context);
	}

	/**
	 * Starting with the specified starting context, search for and return the parent context
	 * of the specified context.
	 *
	 * @param startingContext
	 * @param context
	 * @return the parent context of the specified context, or null
	 * if not found.
	 */
	public static Context getParentContext(Context startingContext, Context context) {
		for (Iterator iter = startingContext.getSubContexts().iterator(); iter.hasNext(); ) {
			Context child = (Context) iter.next();
			if (child.equals(context)) return startingContext;
			Context parent = getParentContext(child, context);
			if (parent != null) return parent; 
		}
		return null;
	}

//	public static void setCurrentRunId(Object runId) {
//		currentRunId = runId;
//		runIdContextTable.put(runId, new ArrayList<Context<?>>());
//	}
//	
//	public static void registerRootContext(Context context) {
//		registerRootContext(context, currentRunId);
//	}
//	
//	public static void registerRootContext(Context context, Object runId) {
//		rootContexts.add(context);
//		if (runIdContextTable.get(runId) == null) {
//			runIdContextTable.put(runId, new ArrayList<Context<?>>());
//		}
//		runIdContextTable.get(runId).add(context);
//	}
//	
//	public static void unregisterRootContext(Context context) {
//		for (ArrayList<Context<?>> contextList : runIdContextTable.values()) {
//			if (contextList.contains(context)) {
//				contextList.remove(context);
//			}
//		}
//		rootContexts.remove(context);
//	}
//	
//	public static void unregisterRootContexts(Object runId) {
//		for (Context context : runIdContextTable.get(runId)) {
//			unregisterRootContext(context);
//		}
//		runIdContextTable.remove(runId);
//	}
}
