/*CopyrightHere*/
package repast.simphony.parameter;

import junit.framework.TestCase;

public class IntSteppedTest extends TestCase {
	IntSteppedSetter init;
	private Parameters params;

	@Override
	protected void setUp() throws Exception {
		this.init = new IntSteppedSetter("x", 0, 10, 1);
		ParametersCreator creator = new ParametersCreator();
		creator.addParameter("x", Integer.class, 0, false);
		params = creator.createParameters();
	}

	public void testForwards(boolean ignoreBeginningTest) {
		assertTrue(ignoreBeginningTest || init.atBeginning());
		init.reset(params);
		assertEquals(0, params.getValue("x"));

		for (int i = 1; i <= 10; i++) {
			init.next(params);
			assertEquals(i, params.getValue("x"));
			assertFalse(init.atBeginning());
		}
		assertTrue(init.atEnd());
	}

	public void testForwardsThenBackwards() {
		testForwards();
		assertFalse(init.atBeginning());
		for (int i = 9 ; i > 0; i--) {
			init.previous(params);
			assertEquals(i, params.getValue("x"));
			assertFalse(init.atBeginning());
		}

		init.previous(params);
		assertEquals(0, params.getValue("x"));
		assertTrue(init.atBeginning());
	}

	public void testForwards() {
		testForwards(false);
		testForwards(true);
	}

	public void testForwardRevert() {
		assertTrue(init.atBeginning());
		init.reset(params);
		assertEquals(0, params.getValue("x"));

		for (int i = 1; i <= 10; i++) {
			init.next(params);
			assertEquals(i, params.getValue("x"));

			init.revert(params);
			assertEquals(i - 1, params.getValue("x"));

			init.next(params);
			assertEquals(i, params.getValue("x"));
		}

		assertTrue(init.atEnd());
	}

	public void testMixedWithRevert() {
		assertTrue(init.atBeginning());
		init.reset(params);
		assertEquals(0, params.getValue("x"));

		for (int i = 1; i <= 5; i++) {
			init.next(params);
			assertEquals(i, params.getValue("x"));

			init.revert(params);
			assertEquals(i - 1, params.getValue("x"));

			init.next(params);
			assertEquals(i, params.getValue("x"));
		}
		// next should give 6

		init.previous(params);
		assertEquals(4, params.getValue("x"));

		init.revert(params);
		assertEquals(5, params.getValue("x"));

		init.next(params);
		assertEquals(6, params.getValue("x"));

		init.revert(params);
		assertEquals(5, params.getValue("x"));

		init.previous(params);
		assertEquals(4, params.getValue("x"));
		init.previous(params);
		assertEquals(3, params.getValue("x"));
		init.previous(params);
		assertEquals(2, params.getValue("x"));
		init.previous(params);
		assertEquals(1, params.getValue("x"));
		init.previous(params);
		assertEquals(0, params.getValue("x"));

		assertTrue(init.atBeginning());
		assertFalse(init.atEnd());

		init.revert(params);
		assertEquals(1, params.getValue("x"));
	}
}
