/*CopyrightHere*/
package repast.simphony.engine.controller;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.Parameters;

/**
 * A null implementation of an
 * {@link repast.simphony.engine.controller.AbstractControllerAction}. This is useful
 * for subclassing when you only want to implement a few methods.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class NullAbstractControllerAction<T> extends AbstractControllerAction<T> {
	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param context
	 *            ignored
	 */
	@Override
	public void runInitialize(RunState runState, Context<? extends T> context, Parameters runParams) {
	}

	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param context
	 *            ignored
	 */
	@Override
	public void runCleanup(RunState runState, Context<? extends T> context) {
	}

}
