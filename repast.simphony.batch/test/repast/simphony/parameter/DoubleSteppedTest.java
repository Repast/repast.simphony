/*CopyrightHere*/
package repast.simphony.parameter;

import junit.framework.TestCase;

public class DoubleSteppedTest extends TestCase {
	DoubleSteppedSetter init;
	private Parameters params;

	@Override
	protected void setUp() throws Exception {
		this.init = new DoubleSteppedSetter("x", 0, 10, 1);
		ParametersCreator creator = new ParametersCreator();
		creator.addParameter("x", Double.class, 0, false);
		params = creator.createParameters();
	}

	public void testForwards(boolean ignoreBeginningTest) {
		assertTrue(ignoreBeginningTest || init.atBeginning());
		init.reset(params);
		assertEquals(0.0, params.getValue("x"));

		for (int i = 1; i <= 10; i++) {
			init.next(params);
			assertEquals(new Double(i), params.getValue("x"));
			assertFalse(init.atBeginning());
		}
		assertTrue(init.atEnd());
	}

	public void testForwardsThenBackwards() {
		testForwards();
		assertFalse(init.atBeginning());
		for (int i = 9 ; i > 0; i--) {
			init.previous(params);
			assertEquals(new Double(i), params.getValue("x"));
			assertFalse(init.atBeginning());
		}

		init.previous(params);
		assertEquals(0.0, params.getValue("x"));
		assertTrue(init.atBeginning());
	}

	public void testForwards() {
		testForwards(false);
		testForwards(true);
	}

	public void testForwardRevert() {
		assertTrue(init.atBeginning());
		init.reset(params);
		assertEquals(0.0, params.getValue("x"));

		for (int i = 1; i <= 10; i++) {
			init.next(params);
			assertEquals(new Double(i), params.getValue("x"));

			init.revert(params);
			assertEquals(new Double(i - 1), params.getValue("x"));

			init.next(params);
			assertEquals(new Double(i), params.getValue("x"));
		}

		assertTrue(init.atEnd());
	}

	public void testMixedWithRevert() {
		assertTrue(init.atBeginning());
		init.reset(params);
		assertEquals(0.0, params.getValue("x"));

		for (int i = 1; i <= 5; i++) {
			init.next(params);
			assertEquals(new Double(i), params.getValue("x"));

			init.revert(params);
			assertEquals(new Double(i - 1), params.getValue("x"));

			init.next(params);
			assertEquals(new Double(i), params.getValue("x"));
		}
		// next should give 6

		init.previous(params);
		assertEquals(4.0, params.getValue("x"));

		init.revert(params);
		assertEquals(5.0, params.getValue("x"));

		init.next(params);
		assertEquals(6.0, params.getValue("x"));

		init.revert(params);
		assertEquals(5.0, params.getValue("x"));

		init.previous(params);
		assertEquals(4.0, params.getValue("x"));
		init.previous(params);
		assertEquals(3.0, params.getValue("x"));
		init.previous(params);
		assertEquals(2.0, params.getValue("x"));
		init.previous(params);
		assertEquals(1.0, params.getValue("x"));
		init.previous(params);
		assertEquals(0.0, params.getValue("x"));

		assertTrue(init.atBeginning());
		assertFalse(init.atEnd());

		init.revert(params);
		assertEquals(1.0, params.getValue("x"));
	}

	public void testPrecise() {
		init = new DoubleSteppedSetter("x", -1.0, 1.0, (1.0 / 3.0));

		init.reset(params);
		assertEquals(-1.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(-2.0/3.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(-1.0/3.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(0.0/3.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(1.0/3.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(2.0/3.0, (Double) params.getValue("x"), .0000001);

		init.next(params);
		assertEquals(1.0, (Double) params.getValue("x"), .0000001);

		assertTrue(init.atEnd());

		init = new DoubleSteppedSetter("x", .8, 1.0, .05);

		System.out.println(.8);

		init.reset(params);
		assertEquals(.8, params.getValue("x"));

		assertFalse(init.atEnd());
		init.next(params);
		System.out.println(params.getValue("x"));
		assertEquals(.85, params.getValue("x"));

		assertFalse(init.atEnd());

		init.next(params);
		assertEquals(.9, params.getValue("x"));

		assertFalse(init.atEnd());

		init.next(params);
		assertEquals(.95, params.getValue("x"));

		assertFalse(init.atEnd());

		init.next(params);
		assertEquals(1.0, params.getValue("x"));

		assertTrue(init.atEnd());
	}
}
