/*CopyrightHere*/
package repast.simphony.parameter;

import junit.framework.TestCase;

public class ListInitializerTest extends TestCase {
	
	private ListParameterSetter<String> initializer;
	private String[] list;
	private Parameters params;

	@Override
	protected void setUp() throws Exception {
		list = new String[] { "s0", "s1", "s2", "s3" };
		initializer = new ListParameterSetter<String>("paramName", list);

		ParametersCreator creator = new ParametersCreator();
		creator.addParameter("paramName", String.class, "s0", false);
		params = creator.createParameters();

	}
	
	public void testForwards() {
		assertTrue(initializer.atBeginning());
		assertFalse(initializer.atEnd());
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[0], params.getValue("paramName"));
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[1], params.getValue("paramName"));
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[2], params.getValue("paramName"));

		initializer.next(params);
		assertTrue(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[3], params.getValue("paramName"));
	}
	
	public void testMixed() {
		assertTrue(initializer.atBeginning());
		assertFalse(initializer.atEnd());
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[0], params.getValue("paramName"));
		
		initializer.previous(params);
		assertFalse(initializer.atEnd());
		assertTrue(initializer.atBeginning());
		assertEquals(list[0], params.getValue("paramName"));
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[0], params.getValue("paramName"));
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[1], params.getValue("paramName"));
		
		initializer.next(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[2], params.getValue("paramName"));

		initializer.next(params);
		assertTrue(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[3], params.getValue("paramName"));

		initializer.previous(params);
		assertFalse(initializer.atEnd());
		assertFalse(initializer.atBeginning());
		assertEquals(list[3], params.getValue("paramName"));
	}

}
