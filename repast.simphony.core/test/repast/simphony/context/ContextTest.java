/*CopyrightHere*/
package repast.simphony.context;

import repast.simphony.engine.environment.RunState;
import junit.framework.TestCase;
public class ContextTest extends TestCase {

	/*
	 * Test method for 'repast.util.ContextUtils.getContext(Object)'
	 */
	@SuppressWarnings("unchecked")
	public void testFindParent() {
		Context rootContext = new DefaultContext();
		Context subContextIn = new DefaultContext();
		rootContext.addSubContext(subContextIn);
		Context subSubContextIn = new DefaultContext();
		subContextIn.addSubContext(subSubContextIn);
		
		Context subContextOut = new DefaultContext();
		rootContext.addSubContext(subContextOut);
		
		Object o = new Object();
		subSubContextIn.add(o);
		
		// store the root context and fetch the object's context
		RunState.init().setMasterContext(rootContext);
		assertSame(subSubContextIn, rootContext.findParent(o));
		
		// make sure it no longer returns anything
		subSubContextIn.remove(o);
		assertNull(rootContext.findParent(o));
	}

	@SuppressWarnings("unchecked")
	public void testFindContext() {
		DefaultContext a = new DefaultContext("a");
		DefaultContext aa = new DefaultContext("aa");
		DefaultContext ab = new DefaultContext("ab");
		DefaultContext ac = new DefaultContext("ac");
		
		DefaultContext aaa = new DefaultContext("aaa");
		aa.addSubContext(aaa);
		
		DefaultContext aba = new DefaultContext("aba");
		ab.addSubContext(aba);
		
		a.addSubContext(aa);
		a.addSubContext(ab);
		a.addSubContext(ac);
		
		assertEquals(a, a.findContext("a"));
		assertEquals(ab, a.findContext("ab"));
		assertEquals(aba, a.findContext("aba"));
		assertEquals(aba, ab.findContext("aba"));
		
		assertNull(a.findContext("aca"));
		assertNull(ab.findContext("ac"));
	}
	
//	/*
//	 * Test method for 'repast.util.ContextUtils.setCurrentRunId(Object)'
//	 */
//	public void testSetCurrentRunId() {
//		
//	}
//
//	/*
//	 * Test method for 'repast.util.ContextUtils.registerRootContext(Context)'
//	 */
//	public void testRegisterRootContextContext() {
//
//	}
//
//	/*
//	 * Test method for 'repast.util.ContextUtils.registerRootContext(Context, Object)'
//	 */
//	public void testRegisterRootContextContextObject() {
//
//	}
//
//	/*
//	 * Test method for 'repast.util.ContextUtils.unregisterRootContext(Context)'
//	 */
//	public void testUnregisterRootContext() {
//
//	}
//
//	/*
//	 * Test method for 'repast.util.ContextUtils.unregisterRootContexts(Object)'
//	 */
//	public void testUnregisterRootContexts() {
//
//	}

}
