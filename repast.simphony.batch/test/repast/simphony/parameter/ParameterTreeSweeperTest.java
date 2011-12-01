/*CopyrightHere*/
package repast.simphony.parameter;

import junit.framework.TestCase;

import java.util.Collection;

public class ParameterTreeSweeperTest extends TestCase {

    private Parameters params;
    private ParameterTreeSweeper sweeper;


    @Override
    protected void setUp() throws Exception {
	    ParametersCreator creator = new ParametersCreator();
		creator.addParameter("name", String.class, "nameValue", false);
		creator.addParameter("intRange", Integer.class, 1, false);
		creator.addParameter("doubleRange", Double.class, 1.0 / 3.0, false);
		creator.addParameter("doubleList", Double.class, 100, false);
		params = creator.createParameters();
	    /*
	    ParameterSetter nameInit = new ConstantSetter<String>("name", "nameValue");
        ParameterSetter intRangeInit = new IntSteppedSetter("intRange", 1, 3, 1);
        ParameterSetter doubleRangeInit = new DoubleSteppedSetter("doubleRange", 1.0 / 3.0, 1.0, 1.0 / 3.0);
        ParameterSetter doubleListInit = new ListParameterSetter<Double>("doubleList", new Double[]{100.0, 200.0});
        */

       // params.setValidating(false);
        sweeper = new ParameterTreeSweeper();
    }

    private void incrementSweeper() {
        sweeper.next(params);
        System.out.println(params.getValue("name") + ", " + params.getValue("intRange") + ", " + params.getValue("doubleRange") + ", "
                + params.getValue("doubleList"));
    }

    private void helperTestSweep(Object[][] expected) {
        for (int i = 0; i < expected.length; i++) {
            assertTrue("Sweeper has no more steps.", !sweeper.atEnd());
            incrementSweeper();
            assertEquals(expected[i][0], params.getValue("name"));
            assertEquals(expected[i][1], params.getValue("intRange"));
            assertEquals((Double)expected[i][2], (Double)params.getValue("doubleRange"), .00001);
            assertEquals(expected[i][3], params.getValue("doubleList"));
        }

        if (!sweeper.atEnd()) {
            System.out.println("Failure: sweeper steps remain:");
            while (!sweeper.atEnd()) {
                incrementSweeper();
            }
            fail("Sweeper steps remain. See above.");
        }
        assertFalse(!sweeper.atEnd());
    }

	public void testRootCount() {
		ParameterSetter nameInit = new ConstantSetter<String>("name", "nameValue");
		ParameterSetter root = sweeper.getRootParameterSetter();
		sweeper.add(root, nameInit);
		Collection<ParameterSetter> children = sweeper.getChildren(root);
		assertEquals(1, children.size());
	}

    /*
     * Test method for 'repast.simphony.parameter.sweep.DefaultParameter!sweeper.atEnd()'
     */
    public void testExpectedOutputChain() {
        ParameterSetter nameInit = new ConstantSetter<String>("name", "nameValue");
        ParameterSetter intRangeInit = new IntSteppedSetter("intRange", 1, 3, 1);
        ParameterSetter doubleRangeInit = new DoubleSteppedSetter("doubleRange", 1.0 / 3.0, 1.0, 1.0 / 3.0);
        ParameterSetter doubleListInit = new ListParameterSetter<Double>("doubleList", new Double[]{100.0, 200.0});
        sweeper.add(sweeper.getRootParameterSetter(), nameInit);
        sweeper.add(nameInit, intRangeInit);
        sweeper.add(intRangeInit, doubleRangeInit);
        sweeper.add(doubleRangeInit, doubleListInit);

        Object[][] expectedValuesChain = new Object[][] {
                { "nameValue", 1, 1.0 / 3.0, 100.0},
                { "nameValue", 1, 1.0 / 3.0, 200.0},
                { "nameValue", 1, 2.0 / 3.0, 100.0},
                { "nameValue", 1, 2.0 / 3.0, 200.0},
                { "nameValue", 1, 1.0, 100.0},
                { "nameValue", 1, 1.0, 200.0},
                { "nameValue", 2, 1.0 / 3.0, 100.0},
                { "nameValue", 2, 1.0 / 3.0, 200.0},
                { "nameValue", 2, 2.0 / 3.0, 100.0},
                { "nameValue", 2, 2.0 / 3.0, 200.0},
                { "nameValue", 2, 1.0, 100.0},
                { "nameValue", 2, 1.0, 200.0},
                { "nameValue", 3, 1.0 / 3.0, 100.0},
                { "nameValue", 3, 1.0 / 3.0, 200.0},
                { "nameValue", 3, 2.0 / 3.0, 100.0},
                { "nameValue", 3, 2.0 / 3.0, 200.0},
                { "nameValue", 3, 1.0, 100.0},
                { "nameValue", 3, 1.0, 200.0},
        };
        helperTestSweep(expectedValuesChain);
    }

    /*
     * Test method for 'repast.simphony.parameter.sweep.DefaultParameter!sweeper.atEnd()'
     */
    public void testExpectedOutputChainUsingConvenienceMethod() {
        params.setValue("name", "nameValue");
        params.setValue("doubleList", 100.0);
        sweeper.addIntRange("intRange", 1, 3, 1);
        sweeper.addDoubleRange("doubleRange", 0.5, 2.0, .5);

        Object[][] expectedValuesChain = new Object[][] {
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1, 1.0, 100.0},
                { "nameValue", 1, 1.5, 100.0},
                { "nameValue", 1, 2.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2, 1.0, 100.0},
                { "nameValue", 2, 1.5, 100.0},
                { "nameValue", 2, 2.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3, 1.0, 100.0},
                { "nameValue", 3, 1.5, 100.0},
                { "nameValue", 3, 2.0, 100.0},
        };
        helperTestSweep(expectedValuesChain);
    }

    /*
     * Test method for 'repast.simphony.parameter.sweep.DefaultParameter!sweeper.atEnd()'
     */
    public void testExpectedOutputRuns() {
        params.setValue("name", "nameValue");
        params.setValue("intRange", 1);
        params.setValue("doubleRange", .5);
        params.setValue("doubleList", 100.0);
        sweeper.addIntRange("intRange", 1, 3, 1);

        Object[][] expectedValuesChain = new Object[][] {
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 3,  .5, 100.0},
        };
        helperTestSweep(expectedValuesChain);

        sweeper.reset(params);
        sweeper.setRunCount(2);
        expectedValuesChain = new Object[][] {
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 3,  .5, 100.0}
        };
        helperTestSweep(expectedValuesChain);

        sweeper.reset(params);
        sweeper.addDoubleRange("doubleRange", 0.5, 1.0, 0.5);
        expectedValuesChain = new Object[][] {
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1,  1.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2,  1.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3,  1.0, 100.0},
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1,  1.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2,  1.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3,  1.0, 100.0},
        };
        helperTestSweep(expectedValuesChain);

        sweeper.reset(params);
        sweeper.setRunCount(3);
        expectedValuesChain = new Object[][] {
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1,  1.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2,  1.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3,  1.0, 100.0},
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1,  1.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2,  1.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3,  1.0, 100.0},
                { "nameValue", 1,  .5, 100.0},
                { "nameValue", 1,  1.0, 100.0},
                { "nameValue", 2,  .5, 100.0},
                { "nameValue", 2,  1.0, 100.0},
                { "nameValue", 3,  .5, 100.0},
                { "nameValue", 3,  1.0, 100.0},
        };
        helperTestSweep(expectedValuesChain);    }

    /*
     * Test method for 'repast.simphony.parameter.sweep.DefaultParameter!sweeper.atEnd()'
     */
    public void testExpectedOutputSimul() {
        ParameterSetter nameInit = new ConstantSetter<String>("name", "nameValue");
        ParameterSetter intRangeInit = new IntSteppedSetter("intRange", 3, 10, 1);
        ParameterSetter doubleRangeInit = new DoubleSteppedSetter("doubleRange", .5, 1.5, .5);
        ParameterSetter doubleListInit = new ListParameterSetter<Double>("doubleList", new Double[]{0d, .5, 1.5, 2d});
        ParameterSetter runInit = new RunParameterSetter(2);
	    ParameterSetter root = sweeper.getRootParameterSetter();
	    sweeper.add(root, runInit);
        sweeper.add(runInit, doubleListInit);
        sweeper.add(root, doubleRangeInit);
        sweeper.add(root, nameInit);
        sweeper.add(root, intRangeInit);

        Object[][] expectedValuesSimul = new Object[][] {
                { "nameValue", 3, 0.5d, 0d },
                { "nameValue", 4, 1.0d, .5d },
                { "nameValue", 5, 1.5d, 1.5d },
                { "nameValue", 6, 1.5d, 2d },
                { "nameValue", 7, 1.5d, 0d },
                { "nameValue", 8, 1.5d, .5d },
                { "nameValue", 9, 1.5d, 1.5d },
                { "nameValue", 10, 1.5d, 2d },
        };
        helperTestSweep(expectedValuesSimul);
    }
}
