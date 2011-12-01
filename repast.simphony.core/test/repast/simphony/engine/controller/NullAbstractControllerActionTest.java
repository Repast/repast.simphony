/*CopyrightHere*/
package repast.simphony.engine.controller;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.engine.controller.NullAbstractControllerAction;

/**
* Tests for {@link repast.simphony.engine.controller.NullAbstractControllerAction}.
*
* @author Jerry Vos
* @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:35 $
*/
public class NullAbstractControllerActionTest extends TestCase {
	NullAbstractControllerAction nullAction;
	
	@Override
	protected void setUp() throws Exception {
		nullAction = new NullAbstractControllerAction();
	}
	
	/*
	 * Test method for 'repast.core.action.NullAbstractControllerAction.batchInitialize(RunState, Context<? extends T>)'
	 */
	@SuppressWarnings("unchecked")
	public void testBatchInitializeRunStateContextOfQextendsT() {
		// does nothing
		nullAction.batchInitialize(null, (Context) null);
	}

	/*
	 * Test method for 'repast.core.action.NullAbstractControllerAction.runInitialize(RunState, Context<? extends T>)'
	 */
	@SuppressWarnings("unchecked")
	public void testRunInitializeRunStateContextOfQextendsT() {
		// does nothing
		nullAction.runInitialize(null, (Context) null, null);
	}

	/*
	 * Test method for 'repast.core.action.NullAbstractControllerAction.runCleanup(RunState, Context<? extends T>)'
	 */
	@SuppressWarnings("unchecked")
	public void testRunCleanupRunStateContextOfQextendsT() {
		// does nothing
		nullAction.runCleanup(null, (Context) null);
	}

	/*
	 * Test method for 'repast.core.action.NullAbstractControllerAction.batchCleanup(RunState, Context<? extends T>)'
	 */
	@SuppressWarnings("unchecked")
	public void testBatchCleanupRunStateContextOfQextendsT() {
		// does nothing
		nullAction.batchCleanup(null, (Context) null);
	}

}
