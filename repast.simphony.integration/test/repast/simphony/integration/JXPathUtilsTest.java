/*CopyrightHere*/
package repast.simphony.integration;

import java.util.ArrayList;

import junit.framework.TestCase;

import org.apache.commons.jxpath.JXPathContext;

public class JXPathUtilsTest extends TestCase {

	private Top rootObject;
	private JXPathContext rootContext;
	private JXPathContext subContext;
	private Object subObject;
	
	public class Top {
		Object bottom = "bottom";
		
		public Object getBottom() {
			return bottom;
		}
	}

	@Override
	protected void setUp() throws Exception {
		rootObject = new Top();
		subObject = rootObject.bottom;
		subContext = JXPathUtils.createContext(subObject, true);
		rootContext = JXPathUtils.createRootContext(rootObject, true);
	}
	
	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.createContext(Object, boolean)'
	 */
	public void testCreateContext() {
		subContext = JXPathUtils.createContext(subObject, true);
		assertTrue(subContext.isLenient());
		assertSame(subContext.getContextBean(), subObject);
	}

	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.createRootContext(Object, boolean)'
	 */
	public void testCreateRootContext() {
		rootContext = JXPathUtils.createRootContext(rootObject, true);
		assertTrue(rootContext.isLenient());
		assertSame(rootObject, ((FileDef) rootContext.getContextBean()).getFileDef());
	}

	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.getContext(JXPathContext, JXPathContext, String)'
	 */
	public void testGetContext() {
		assertSame(subContext, JXPathUtils.getContext(rootContext, subContext, "asdf/asdf/asdf"));
		assertSame(subContext, JXPathUtils.getContext(rootContext, subContext, "asdf"));
		
		assertSame(rootContext, JXPathUtils.getContext(rootContext, subContext, "/asdf/asdf"));
		assertSame(rootContext, JXPathUtils.getContext(rootContext, subContext, "/asdf"));
		
		assertSame(rootContext, JXPathUtils.getContext(rootContext, null, "/asdf"));
		assertSame(subContext, JXPathUtils.getContext(null, subContext, "/asdf"));
		
		assertSame(rootContext, JXPathUtils.getContext(rootContext, null, "asdf"));
		assertSame(subContext, JXPathUtils.getContext(null, subContext, "asdf"));
	}

	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.fixPath(String)'
	 */
	public void testFixPath() {
		String correctPath = "/fileDef/asdf/eras9/asdfn";
		String badPath = "/fileDef/asdf/eras9/asdfn";
		
		assertEquals(correctPath, JXPathUtils.fixPath(badPath));
		assertEquals(correctPath, JXPathUtils.fixPath(correctPath));
	}

	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.getXPathNode(JXPathContext, JXPathContext, String)'
	 */
	public void testGetXPathNode() {
		assertEquals(rootContext.getContextBean(), JXPathUtils.getXPathNode(rootContext, subContext, "/"));
		assertEquals(subObject, JXPathUtils.getXPathNode(rootContext, rootContext, "fileDef/bottom"));
		assertEquals(subObject, JXPathUtils.getXPathNode(rootContext, subContext, "/fileDef/bottom"));
		assertNull(JXPathUtils.getXPathNode(rootContext, subContext, "/non-existantfield"));
	}

	public void testGetXPathNodeExcept() {
		try {
			JXPathUtils.getXPathNode(null, null, "/fileDef");
			fail();
		} catch (RuntimeException ex) {
			// should happen because both contexts were null
		}
	}
	
	public void testGetXPathNodesExcept() {
		try {
			JXPathUtils.getXPathNodes(null, null, "/fileDef");
			fail();
		} catch (RuntimeException ex) {
			// should happen because both contexts were null
		}
	}
	
	@SuppressWarnings("unchecked")
	static ArrayList toList(Object obj) {
		ArrayList foo = new ArrayList();
		
		foo.add(obj);
		return foo;
	}
	
	/*
	 * Test method for 'repast.simphony.integration.JXPathUtils.getXPathNodes(JXPathContext, JXPathContext, String)'
	 */
	public void testGetXPathNodes() {
		assertEquals(toList(rootContext.getContextBean()), JXPathUtils.getXPathNodes(rootContext, subContext, "/"));
		assertEquals(toList(subObject), JXPathUtils.getXPathNodes(rootContext, rootContext, "fileDef/bottom"));
		assertEquals(toList(subObject), JXPathUtils.getXPathNodes(rootContext, subContext, "/fileDef/bottom"));
		assertTrue(JXPathUtils.getXPathNodes(rootContext, subContext, "/non-existantfield").isEmpty());
	}

}
