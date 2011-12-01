package repast.simphony.parameter;

import junit.framework.TestCase;

import java.util.*;


/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ParameterSweeperBuilderTest extends TestCase {

	private ParameterSweeperBuilder builder;

	public void setUp() {
		builder = new ParameterSweeperBuilder();
	}

	public void testRuns() {
		builder.setRunCount(3);
		assertEquals(3, builder.getSweeper().getRunCount());
	}

	public void testIntStepper() {
		ParameterSetter setter = builder.addStepper("p", 1, 3, 1);
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals(1, params.getValue("p"));
		setter.next(params);
		assertEquals(2, params.getValue("p"));
		setter.next(params);
		assertEquals(3, params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof IntSteppedSetter);
		assertEquals(setter, builder.getSetter("p"));
	}

	public void testDoubleStepper() {
		ParameterSetter setter = builder.addStepper("p", 1.0, 1.2, .1);
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals(1.0, params.getValue("p"));
		setter.next(params);
		assertEquals(1.1, params.getValue("p"));
		setter.next(params);
		assertEquals(1.2, params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof DoubleSteppedSetter);
		assertEquals(setter, builder.getSetter("p"));
	}

  public void testDoubleStepper2() {
    ParameterSetter setter = builder.addStepper("p", 1.0, 3, 1);
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals(new Double(1.0), params.getValue("p"));
		setter.next(params);
		assertEquals(new Double(2.0), params.getValue("p"));
		setter.next(params);
		assertEquals(new Double(3.0), params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof DoubleSteppedSetter);
		assertEquals(setter, builder.getSetter("p"));
  }

  public void testFloatStepper() {
		ParameterSetter setter = builder.addStepper("p", 1.0f, 1.2f, .1f);
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals(1.0f, params.getValue("p"));
		setter.next(params);
		assertEquals(1.1f, params.getValue("p"));
		setter.next(params);
		assertEquals(1.2f, params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof FloatSteppedSetter);
		assertEquals(setter, builder.getSetter("p"));
	}

	public void testLongStepper() {
		ParameterSetter setter = builder.addStepper("p", 1L, 3L, 1L);
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals(1L, params.getValue("p"));
		setter.next(params);
		assertEquals(2L, params.getValue("p"));
		setter.next(params);
		assertEquals(3L, params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof LongSteppedSetter);
		assertEquals(setter, builder.getSetter("p"));
	}

	public void testListSetter() {
		ParameterSetter setter = builder.addListSetter("p", new String[] {"cormac", "nicola", "me"});
		Parameters params = builder.getParameters();
		setter.reset(params);
		assertEquals("cormac", params.getValue("p"));
		setter.next(params);
		assertEquals("nicola", params.getValue("p"));
		setter.next(params);
		assertEquals("me", params.getValue("p"));
		assertTrue(setter.atEnd());
		assertTrue(setter instanceof ListParameterSetter);
		assertEquals(setter, builder.getSetter("p"));
	}


	public void testParameterTree() {
		try {
			ParameterSweeperBuilder builder = new ParameterSweeperBuilder();
			builder.setRunCount(2);
			builder.addConstant("const_1", .3f);
			builder.addConstant("const_2", .2);
			builder.addConstant("const_3", 11);
			builder.addConstant("const_4", 11L);
			builder.addConstant("const_5", "hello cormac");
			builder.addConstant("const_6", false);

			ParameterSetter top = builder.addStepper("num_1", 1L, 4, 1);
			ParameterSetter num_2 = builder.addStepper("num_2", .3f, 1.0f, .1f);
			builder.addListSetter(num_2, "list_val", new String[]{"foo", "bar", "baz"});
			builder.addStepper(top, "num_3", .9, 1.0, .1);
			builder.addStepper(top, "num_4", 2, 3, 1);

			ParameterTreeSweeper sweeper = builder.getSweeper();
			assertEquals(2, sweeper.getRunCount());
			ParameterSetter root = sweeper.getRootParameterSetter();
			Collection<ParameterSetter> children = sweeper.getChildren(root);
			assertEquals(7, children.size());

			Set<String> names = new HashSet<String>();
			LongSteppedSetter iSetter = null;
			for (ParameterSetter setter : children) {
				 if (setter instanceof LongSteppedSetter) {
					 iSetter = (LongSteppedSetter)setter;
					 names.add(((LongSteppedSetter)setter).getParameterName());
				 }
				 if (setter instanceof ConstantSetter) names.add(((ConstantSetter)setter).getParameterName());
			}
			assertTrue(names.contains("num_1"));
			assertTrue(names.contains("const_1"));
			assertTrue(names.contains("const_2"));
			assertTrue(names.contains("const_3"));
			assertTrue(names.contains("const_4"));
			assertTrue(names.contains("const_5"));
			assertTrue(names.contains("const_6"));

			assertEquals("num_1", iSetter.getParameterName());

			children = sweeper.getChildren(iSetter);
			assertEquals(3, children.size());
			Set<Class> types = new HashSet<Class>();
			types.add(DoubleSteppedSetter.class);
			types.add(FloatSteppedSetter.class);
			types.add(IntSteppedSetter.class);
			Map<String, ParameterSetter> map = new HashMap<String, ParameterSetter>();
			for (ParameterSetter ps : children) {
				assertTrue(types.remove(ps.getClass()));
				if (ps instanceof DoubleSteppedSetter) map.put(((DoubleSteppedSetter)ps).getParameterName(), ps);
				if (ps instanceof FloatSteppedSetter) map.put(((FloatSteppedSetter)ps).getParameterName(), ps);
				if (ps instanceof IntSteppedSetter) map.put(((IntSteppedSetter)ps).getParameterName(), ps);
			}
			assertEquals(0, types.size());
			assertTrue(map.containsKey("num_2"));
			assertTrue(map.containsKey("num_3"));
			assertTrue(map.containsKey("num_4"));

			children = sweeper.getChildren(map.get("num_2"));
			assertEquals(1, children.size());
			ListParameterSetter<String> lSetter = (ListParameterSetter<String>) children.iterator().next();
			assertEquals("list_val", lSetter.getParameterName());
			children = sweeper.getChildren(lSetter);
			assertEquals(0, children.size());

			children = sweeper.getChildren(map.get("num_3"));
			assertEquals(0, children.size());
			children = sweeper.getChildren(map.get("num_4"));
			assertEquals(0, children.size());

			Parameters params = builder.getParameters();
			names = new HashSet<String>();
			names.add("num_1");
			names.add("num_2");
			names.add("num_3");
			names.add("num_4");
			names.add("list_val");
			names.add("const_1");
			names.add("const_2");
			names.add("const_3");
			names.add("const_4");
			names.add("const_5");
			names.add("const_6");
			for (String name : params.getSchema().parameterNames()) {
				assertTrue(names.remove(name));
			}

			assertEquals(new Long(1), params.getValue("num_1"));
			assertEquals(new Float(.3), params.getValue("num_2"));
			assertEquals(new Double(.9), params.getValue("num_3"));
			assertEquals(new Integer(2), params.getValue("num_4"));
			assertEquals(new Float(.3f), params.getValue("const_1"));
			assertEquals(new Double(.2), params.getValue("const_2"));
			assertEquals(new Integer(11), params.getValue("const_3"));
			assertEquals(new Long(11), params.getValue("const_4"));
			assertEquals("hello cormac", params.getValue("const_5"));
			assertEquals(Boolean.FALSE, params.getValue("const_6"));

		} catch (Exception ex) {
			ex.printStackTrace();
			assertTrue(false);
		}
	}

}
