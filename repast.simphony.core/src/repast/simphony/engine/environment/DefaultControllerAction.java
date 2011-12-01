/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.parameter.Parameters;

/**
 * A null implementation of a ControllerAction. This provides no functionality,
 * but can be used for subclassing purposes.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultControllerAction implements ControllerAction {
	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param contextId
	 *            ignored
	 */
	public void batchInitialize(RunState runState, Object contextId) {
	}

	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param contextId
	 *            ignored
	 * @param runParams
	 *            ignored
	 */
	public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
	}

	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param contextId
	 *            ignored
	 */
	public void batchCleanup(RunState runState, Object contextId) {
	}

	/**
	 * Does nothing.
	 * 
	 * @param runState
	 *            ignored
	 * @param contextId
	 *            ignored
	 */
	public void runCleanup(RunState runState, Object contextId) {
	}
}
