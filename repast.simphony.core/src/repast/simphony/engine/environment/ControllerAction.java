/*CopyrightHere*/
package repast.simphony.engine.environment;

import java.io.Serializable;

import repast.simphony.parameter.Parameters;

/**
 * An action performed by the Controller. This action performs initialization
 * and cleanup activities for the setup/tear-down of a simulation.<p/>
 * 
 * The batch methods are called once for every set of runs in a batch run. Run
 * methods are called once for every run. For example, a batch run could have 50
 * runs occur in it, one for each set of parameters in a parameter sweep. The
 * batch methods would be called once, while the run methods would be called 50
 * times.
 * 
 * @author Jerry Vos
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface ControllerAction {
	/**
	 * Initializes the action for a batch run (a set of runs).
	 * 
	 * @param runState
	 */
	void batchInitialize(RunState runState, Object contextId);

	/**
	 * Initializes the action for a single run (possibly one of many).
	 * 
	 * @param runState
	 * @param runParams
	 *            the current run's parameters
	 */
	void runInitialize(RunState runState, Object contextId, Parameters runParams);

	/**
	 * Cleans up the action after a run just occurred.
	 * 
	 * @param runState
	 *            information on the run that just occurred
	 */
	void runCleanup(RunState runState, Object contextId);

	/**
	 * Cleans up the action after a batch run (a set of runs).
	 * 
	 * @param runState
	 *            information on the set of runs that just occurred
	 */
	void batchCleanup(RunState runState, Object contextId);
}
