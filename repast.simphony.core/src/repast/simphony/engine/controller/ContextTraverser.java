/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.context.Context;
import repast.simphony.space.graph.Traverser;

import java.util.Iterator;

/**
 * A {@link repast.simphony.space.graph.Traverser} that traverses Contexts by returning
 * the subContexts of passed in Contexts.
 *
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class ContextTraverser implements Traverser<Context> {
	// TODO: fix the generics on this
	/**
	 * Returns the subContexts of the currentContext.
	 * 
	 * @see Context#getSubContexts()
	 * 
	 * @param previousContext ignored
	 * @param currentContext the context who's subContexts to return
	 */
	public Iterator<Context> getSuccessors(Context previousContext,
			Context currentContext) {
		return currentContext.getSubContexts().iterator();
	}

	/**
	 * Currently just returns 0.
	 * 
	 * @param fromNode
	 *            ignored
	 * @param toNode
	 *            ignored
	 * @return 0
	 */
	public double getDistance(Context fromNode, Context toNode) {
		return 0;
	}

}
