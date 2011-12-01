/*CopyrightHere*/
package repast.simphony.engine.controller;

import junit.framework.TestCase;
import repast.simphony.engine.environment.DefaultControllerAction;

/**
* Tests for {@link repast.simphony.engine.environment.DefaultControllerAction}.
*
* @author Jerry Vos
* @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
*/
public class NullControllerActionTest extends TestCase {
	DefaultControllerAction nullAction;
	
	@Override
	protected void setUp() throws Exception {
		nullAction = new DefaultControllerAction();
	}
	
	/*
	 * Test method for 'repast.core.action.NullControllerAction.batchInitialize(RunState, Object)'
	 */
	public void testBatchInitialize() {
		// no effect
		nullAction.batchInitialize(null, null);
	}

	/*
	 * Test method for 'repast.core.action.NullControllerAction.runInitialize(RunState, Object)'
	 */
	public void testRunInitialize() {
		// no effect
		nullAction.runInitialize(null, null, null);
	}

	/*
	 * Test method for 'repast.core.action.NullControllerAction.batchCleanup(RunState, Object)'
	 */
	public void testBatchCleanup() {
		// no effect
		nullAction.batchCleanup(null, null);
	}

	/*
	 * Test method for 'repast.core.action.NullControllerAction.runCleanup(RunState, Object)'
	 */
	public void testRunCleanup() {
		// no effect
		nullAction.runCleanup(null, null);
	}

}
