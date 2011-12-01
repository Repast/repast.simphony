/*CopyrightHere*/
package repast.simphony.integration;

import java.util.Iterator;

import junitx.util.PrivateAccessor;

import org.apache.commons.jxpath.JXPathContext;
import org.jmock.MockObjectTestCase;

public class BeanQueryerTest extends MockObjectTestCase {
	
	
	private BeanQueryer queryer;
	private Object root;
	private JXPathContext rootObjectContext;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	
		root = new QueriedClass();
		rootObjectContext = JXPathContext.newContext(root);
		queryer = new BeanQueryer(root);
	}
	
	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.BeanQueryer(Object)'
	 */
	public void testBeanQueryer() throws NoSuchFieldException {
		assertSame(root, ((FileDef) ((JXPathContext) PrivateAccessor.getField(queryer, "rootContext")).getContextBean()).getFileDef());
	}

	static Object field1 = "field1Value";
	static Object field2 = "field2Value";
	
	public static class QueriedClass {
		public Object getField1() {
			return field1;
		}
		
		public Object getField2() {
			return field2;
		}
	}
	
	public void testSelectNodeString() {
		assertEquals(field1, queryer.selectNode("/fileDef/field1"));
		assertEquals(field2, queryer.selectNode("/fileDef/field2"));
		
		assertNull(queryer.selectNode("/fileDef/field101293asfasf"));
	}

	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.selectNode(Object, String)'
	 */
	public void testSelectNodeObjectString() {
		assertEquals(field1, queryer.selectNode(rootObjectContext, "/fileDef/field1"));
		assertEquals(field1, queryer.selectNode(rootObjectContext, "field1"));
		
		assertNull(queryer.selectNode(rootObjectContext, "/fileDef/field101293asfasf"));
		
		assertEquals(field1, queryer.selectNode(rootObjectContext.getContextBean(), "/fileDef/field1"));
		assertEquals(field1, queryer.selectNode(rootObjectContext.getContextBean(), "field1"));

		assertEquals(field1, queryer.selectNode(queryer.selectNode("/"), "/fileDef/field1"));
	}

	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.selectNodes(String)'
	 */
	public void testSelectNodesString() {
		Iterator<?> iter = queryer.selectNodes("/fileDef/field1").iterator();

		assertEquals(field1, iter.next());
		assertFalse(iter.hasNext());
		
		iter = queryer.selectNodes("/fileDef/field2").iterator();

		assertEquals(field2, iter.next());
		assertFalse(iter.hasNext());

		assertFalse(queryer.selectNodes("/fileDef/field101293asfasf").iterator().hasNext());
	}

	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.selectNodes(Object, String)'
	 */
	public void testSelectNodesObjectString() {
		Iterator<?> iter = queryer.selectNodes(rootObjectContext, "/fileDef/field1").iterator();

		assertEquals(field1, iter.next());
		assertFalse(iter.hasNext());
		
		iter = queryer.selectNodes(rootObjectContext, "field1").iterator();

		assertEquals(field1, iter.next());
		assertFalse(iter.hasNext());

		assertFalse(queryer.selectNodes("field101293asfasf").iterator().hasNext());
		

		iter = queryer.selectNodes(rootObjectContext.getContextBean(), "/fileDef/field1").iterator();

		assertEquals(field1, iter.next());
		assertFalse(iter.hasNext());
	}

	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.getRoot()'
	 */
	public void testGetRoot() {
		assertSame(root, ((FileDef) queryer.getRoot()).getFileDef());
	}

	/*
	 * Test method for 'repast.simphony.integration.BeanQueryer.getValue(Object)'
	 */
	public void testGetValue() {
		assertEquals(field1, queryer.getValue(field1));
		
		assertNull(queryer.getValue(null));
	}

}
