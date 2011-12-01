/*CopyrightHere*/
package repast.simphony.engine.controller;

import org.apache.commons.collections15.Predicate;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.graph.EngineGraphUtilities;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

/**
 * An abstract {@link repast.simphony.engine.environment.ControllerAction} that aims to
 * simplify the implementation of ControllerActions. This class searches the
 * {@link repast.simphony.engine.environment.RunState}
 * 
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public abstract class AbstractControllerAction<T> implements ControllerAction {
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(AbstractControllerAction.class);

	/**
	 * Default empty implementation of this method that performs no action.
	 * If a subclass should perform batch initialization override this method.
	 *
	 * @param runState the runState used for the action
	 * @param contextId the id used for finding the context
	 */
	public void batchInitialize(RunState runState, Object contextId) {
	        // do nothing
	}


	/**
	 * This delegates to {@link #runInitialize(RunState, Context)} after finding
	 * the context with the given contextId.<p/>
	 * 
	 * This is the same as
	 * <code>runInitialize(runState, findContextInRunState(runState, contextId));</code>
	 * 
	 * @param runState
	 *            the runState used for the action
	 * @param contextId
	 *            the id used for finding the context
	 */
	public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
		runInitialize(runState, findContextInRunState(runState, contextId), runParams);
	}

	/**
	 * This delegates to {@link #runCleanup(RunState, Context)} after finding
	 * the context with the given contextId.<p/>
	 * 
	 * This is the same as
	 * <code>runCleanup(runState, findContextInRunState(runState, contextId));</code>
	 * 
	 * @param runState
	 *            the runState used for the action
	 * @param contextId
	 *            the id used for finding the context
	 */
	public void runCleanup(RunState runState, Object contextId) {
		runCleanup(runState, findContextInRunState(runState, contextId));
	}

	/**
	 * This performs no action.
	 * 
	 * @param runState
	 *            the runState used for the action
	 * @param contextId
	 *            the id used for finding the context
	 */
	public void batchCleanup(RunState runState, Object contextId) {
		// do nothing
	}

	public abstract void runInitialize(RunState runState, Context<? extends T> context, Parameters runParams);

	public abstract void runCleanup(RunState runState, Context<? extends T> context);

	@SuppressWarnings("serial")
	public static Context findContextInRunState(RunState runState,
	                                            final Object contextId) {
		if (runState.getMasterContext() == null) {
			RuntimeException e = new RuntimeException(
					"Error, the master context was not "
							+ "initialized properly and is null. A master context must be"
							+ "set.");
			msgCenter
					.error(
							"AbstractControllerAction.findContextInRunState: Error, the master context was not "
									+ "initialized properly and is null. A master context must be"
									+ "set.", e);
			throw e;
		}
		// TODO: convert this to use XPath or some other path'ed descriptor
		// if (contextId instanceof String) {
		//	
		// }
		return EngineGraphUtilities.breadthFirstSearch(
				new Predicate<Context>() {
					public boolean evaluate(Context toCheck) {
						if (toCheck.getId().equals(contextId)) {
							return true;
						} else {
							return false;
						}
					}
				}, new ContextTraverser(), runState.getMasterContext());
	}

	/**
	 * Accepts the specified visitor. This calls the default
	 * visit method.
	 *
	 * @param visitor the visitor to accept
	 */
	public void accept(ControllerActionVisitor visitor) {
		visitor.visit(this);
	}

}
