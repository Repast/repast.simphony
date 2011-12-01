package repast.simphony.util;

import junit.framework.TestCase;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunState;
import repast.simphony.util.ContextUtils;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ContextUtilsTests extends TestCase {

	class TestContext extends DefaultContext {


		public TestContext(Object name) {
			super(name);
		}

		public String toString() {
			return this.getId().toString();
		}
	}

	public void testParentContextFind() {
		RunState.init();
		Context masterContext = new TestContext("Master");
		RunState state = RunState.getInstance();
		state.setMasterContext(masterContext);

		Context child1 = new TestContext("child1");
		Context child2 = new TestContext("child2");
		Context grandChild1 = new TestContext("gc1");
		Context grandChild2 = new TestContext("gc2");
		Context grandChild3 = new TestContext("gc3");

		child1.addSubContext(grandChild1);
		child1.addSubContext(grandChild2);
		child2.addSubContext(grandChild3);

		masterContext.addSubContext(child1);
		masterContext.addSubContext(child2);

		Context context = ContextUtils.getParentContext(grandChild3);
		assertEquals(child2, context);
		context = ContextUtils.getParentContext(child2);
		assertEquals(masterContext, context);
		context = ContextUtils.getParentContext(grandChild2);
		assertEquals(child1, context);



	}
}
