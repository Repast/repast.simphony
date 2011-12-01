/*CopyrightHere*/
package repast.simphony.parameter;

import junit.framework.TestCase;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.controller.DefaultControllerRegistry;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.parameter.optimizer.*;
import repast.simphony.random.RandomHelper;

/**
 * Tests for {@link repast.simphony.parameter.optimizer.OptimizedParameterSweeper} and also some of the
 * {@link repast.simphony.parameter.optimizer.AdvancementChooser}s.
 * 
 * @author Jerry Vos
 */
public class OptimizedParameterTest extends TestCase {
	private static final String MASTER_ID = "masterid";
	private ConstantSetter<String> nameInit;
	private IntSteppedSetter intRangeInit;
	private ListParameterSetter<Double> doubleListInit;
	
	private OptimizedParameterSweeper sweeper;
	private RunState runState;
	private DoubleSteppedSetter doubleRangeInit;
	private Parameters params;
	
	
	@Override
	protected void setUp() throws Exception {
		nameInit = new ConstantSetter<String>("name", "nameValue");
		intRangeInit = new IntSteppedSetter("intRange", 0, 10, 1);
		doubleListInit = new ListParameterSetter<Double>("doubleList", new Double[] { 0.0, .5, 1.5, 2d});
		doubleRangeInit = new DoubleSteppedSetter("doubleRange", 0, 1, .5);
		
		runState = RunState.init();
		runState.setMasterContext(new DefaultContext(MASTER_ID));
		runState.setControllerRegistry(new DefaultControllerRegistry());
		sweeper = new OptimizedParameterSweeper(runState.getControllerRegistry(), MASTER_ID);

		ParameterSetter root = sweeper.getRootParameterSetter();
		sweeper.add(root, doubleListInit);
		sweeper.add(doubleListInit, doubleRangeInit);
		sweeper.add(root, nameInit);
		sweeper.add(root, intRangeInit);
		
		ParametersCreator creator = new ParametersCreator();
		creator.addParameter("name", String.class, "nameValue", false);
		creator.addParameter("intRange", Integer.class, 0, false);
		creator.addParameter("doubleList", Double.class, 0.0, false);
		creator.addParameter("doubleRange", Double.class, 0.0, false);
		creator.addParameter("x", Integer.class, 0, false);
		creator.addParameter("y", Integer.class, 0, false);
		

		params = creator.createParameters();
	}
	
	/*
	 * Test method for 'repast.simphony.parameter.sweep.ParameterTreeSweeper.atEnd()'
	 */
	public void testExpectedOutput() {
		Object[][] expectedValues = new Object[][] {
				{ "nameValue", 0, 0.0, 0.0 },
				{ "nameValue", 0, 0.0, 0.5 },
				{ "nameValue", 0, 0.0, 1.0 },
				{ "nameValue", 1, 0.0, 1.0 },
				{ "nameValue", 2, 0.0, 1.0 },
				{ "nameValue", 3, 0.0, 1.0 },
				{ "nameValue", 4, 0.0, 1.0 },
				{ "nameValue", 5, 0.0, 1.0 },
				{ "nameValue", 6, 0.0, 1.0 },
				{ "nameValue", 7, 0.0, 1.0 },
				{ "nameValue", 8, 0.0, 1.0 },
				{ "nameValue", 9, 0.0, 1.0 },
				{ "nameValue", 10, 0.0, 1.0 },
				{ "nameValue", 10, 0.5, 1.0 },
				{ "nameValue", 10, 1.5, 1.0 },
				{ "nameValue", 10, 2d, 1.0 },
		};
		
		for (int i = 0; i < expectedValues.length; i++) {
			assertTrue(!sweeper.atEnd());
			sweeper.next(params);
			assertEquals(expectedValues[i][0], params.getValue("name"));
			assertEquals(expectedValues[i][1], params.getValue("intRange"));
			assertEquals(expectedValues[i][2], params.getValue("doubleList"));
			assertEquals(expectedValues[i][3], params.getValue("doubleRange"));
		}

		sweeper.next(params);
		
		assertFalse(!sweeper.atEnd());
	}

	public void testWithRevert() {
		Object[][] expectedValues = new Object[][] {
				{ "nameValue", 0, 0.0, 0.0 },
				{ "nameValue", 0, 0.0, 0.5 },
				{ "nameValue", 0, 0.0, 1.0 },
				{ "nameValue", 1, 0.0, 1.0 },
				{ "nameValue", 2, 0.0, 1.0 },
				{ "nameValue", 0, 0.0, 1.0 },	// because of the revert/BACKWARD
												// reverts to 1, backs to 0
				{ "nameValue", 1, 0.0, 1.0 },
				{ "nameValue", 2, 0.0, 1.0 },
				{ "nameValue", 3, 0.0, 1.0 },
				{ "nameValue", 4, 0.0, 1.0 },
				{ "nameValue", 5, 0.0, 1.0 },
				{ "nameValue", 6, 0.0, 1.0 },
				{ "nameValue", 7, 0.0, 1.0 },
				{ "nameValue", 8, 0.0, 1.0 },
				{ "nameValue", 9, 0.0, 1.0 },
				{ "nameValue", 10, 0.0, 1.0 },
				{ "nameValue", 10, 0.5, 1.0 },
				{ "nameValue", 10, 1.5, 1.0 },
				{ "nameValue", 10, 2d, 1.0 },
		};
		
		AdvancementChooser defaultChooser = sweeper.getAdvancementChooser();
		AdvancementChooser backTrackChooser = new AdvancementChooser() {
			public boolean shouldRevert(double runResult) {
				return true;
			}
			public AdvanceType chooseAdvancement(ParameterSetter initializer, AdvanceType lastType, double runResult) {
				return AdvanceType.BACKWARD;
			}
		};
		
		for (int i = 0; i < expectedValues.length; i++) {
			if (i == 5) {
				sweeper.setAdvancementChooser(backTrackChooser);
			} else if (i == 6) {
				sweeper.setAdvancementChooser(defaultChooser);
			}
			assertTrue(!sweeper.atEnd());
			sweeper.next(params);
			assertEquals(expectedValues[i][0], params.getValue("name"));
			assertEquals(expectedValues[i][1], params.getValue("intRange"));
			assertEquals(expectedValues[i][2], params.getValue("doubleList"));
			assertEquals(expectedValues[i][3], params.getValue("doubleRange"));
		}

//		System.out.println("One last next because the optimized sweeper's finish condition is off.");
//		System.out.println(!sweeper.atEnd());
		sweeper.next(params);
//		System.out.println(params.getValue("name") + ", " + params.getValue("intRange") + ", "
//				+ params.getValue("doubleList") + ", " + params.getValue("doubleRange"));
		
		assertFalse(!sweeper.atEnd());
	}
	
	public void testHill() {
		final int xCenter = 20;
		final int yCenter = 25;
		
		sweeper.setResultProducer(new RunResultProducer() {
			public double getRunValue(RunState runState) {
				Parameters params = RunEnvironment.getInstance().getParameters();
				
				double x = (Integer) params.getValue("x") - xCenter;
				double y = (Integer) params.getValue("y") - yCenter;
				
				return -(x * x + y * y); 
			}
		});
		
		RunEnvironment.init(null, null, params, false);
		
		// have to update
		ControllerAction runResultAction = runState.getControllerRegistry().getActionTree(
				MASTER_ID).getChildren(ControllerRegistry.ACTION_TREE_ROOT).iterator().next();
		// uncomment these to get a cleaner run for debugging purposes
//		sweeper.remove(intRangeInit);
//		sweeper.remove(this.doubleRangeInit);
//		sweeper.remove(this.doubleListInit);
//		sweeper.remove(this.nameInit);
		
		sweeper.setAdvancementChooser(new HillClimber());
		sweeper.addToRoot(new IntSteppedSetter("x", 0, 40, 1));
		sweeper.addToRoot(new IntSteppedSetter("y", 0, 40, 1));

		while (!sweeper.atEnd()) {
			sweeper.next(params);
//			System.out.println("x: " + params.getValue("x") + ", y: " + params.getValue("y"));
			runResultAction.runCleanup(runState, MASTER_ID);
		}
		assertEquals(xCenter, params.getValue("x"));
		assertEquals(yCenter, params.getValue("y"));
	}
	
	public void testHillRandomStart() {
		// try with some random initial start point
		System.out.println("testHillRandomStart");
//		Random.setSeed(1L);
		RandomHelper.setSeed((int)System.currentTimeMillis());
		System.out.println(RandomHelper.getSeed());
		sweeper.setFirstStepRandom(true);
		testHill();
	}
	
	public void testRepeatedlyHillRandomStart() throws Exception {
		for (int i = 0; i < 1000; i++) {
			setUp();
			testHillRandomStart();
		}
	}
	
	public void testAnnealingSingleHill() {
		System.out.println("testAnnealingSingleHill");
		final int xCenter = 20;
		final int yCenter = 25;
		
		sweeper.setResultProducer(new RunResultProducer() {
			public double getRunValue(RunState runState) {
				Parameters params = RunEnvironment.getInstance().getParameters();
				
				double x = (Integer) params.getValue("x") - xCenter;
				double y = (Integer) params.getValue("y") - yCenter;
				
				return -(x * x + y * y); 
			}
		});
		
		RunEnvironment.init(null, null, params, false);
		
		// have to update
		ControllerAction runResultAction = runState.getControllerRegistry().getActionTree(
				MASTER_ID).getChildren(ControllerRegistry.ACTION_TREE_ROOT).iterator().next();
		// uncomment these to get a cleaner run for debugging purposes
//		sweeper.remove(intRangeInit);
//		sweeper.remove(this.doubleRangeInit);
//		sweeper.remove(this.doubleListInit);
//		sweeper.remove(this.nameInit);
		
		sweeper.setAdvancementChooser(new AnnealingAdvancementChooser());
		sweeper.addToRoot(new IntSteppedSetter("x", 0, 40, 1));
		sweeper.addToRoot(new IntSteppedSetter("y", 0, 40, 1));
		RandomHelper.setSeed((int)System.currentTimeMillis());
//		Random.setSeed(1149713758950L);
		System.out.println(RandomHelper.getSeed());
		while (!sweeper.atEnd()) {
			sweeper.next(params);
//			System.out.println("x: " + params.getValue("x") + ", y: " + params.getValue("y"));
//			System.out.println(params.getValue("name") + ", " 
//					+ params.getValue("doubleList") + ", " + params.getValue("doubleRange"));
			runResultAction.runCleanup(runState, MASTER_ID);
		}
		assertEquals(xCenter, params.getValue("x"));
		assertEquals(yCenter, params.getValue("y"));
	}
	
	public void testAnnealingRepeatedly() throws Exception {
		for (int i = 0; i < 1000; i++) {
			setUp();
			testAnnealingSingleHill();
		}
	}
		
	public void testHillMultipleHill1() {
		System.out.println("testHillMultipleHill1");
		
		sweeper.setResultProducer(new RunResultProducer() {
			public double getRunValue(RunState runState) {
				Parameters params = RunEnvironment.getInstance().getParameters();
				
				double x = (Integer) params.getValue("x");
				
				// in [0, 30] has global max near x=3 and a local max near x=20, x=30 
				
				return (((((x - 10)*(x - 30))*(x - 15))*x)*(x - 25))/400; 
			}
		});
		
		RunEnvironment.init(null, null, params, false);
		
		// have to update
		ControllerAction runResultAction = runState.getControllerRegistry().getActionTree(
				MASTER_ID).getChildren(ControllerRegistry.ACTION_TREE_ROOT).iterator().next();
		// uncomment these to get a cleaner run for debugging purposes
//		sweeper.remove(intRangeInit);
//		sweeper.remove(this.doubleRangeInit);
//		sweeper.remove(this.doubleListInit);
//		sweeper.remove(this.nameInit);
		
		sweeper.setAdvancementChooser(new HillClimber());
		// find the 1st hill
		sweeper.addToRoot(new IntSteppedSetter("x", 0, 30, 1));
		sweeper.addToRoot(new IntSteppedSetter("y", 0, 30, 1));
		RandomHelper.setSeed((int)System.currentTimeMillis());
//		Random.setSeed(1149713758950L);
		System.out.println(RandomHelper.getSeed());
		while (!sweeper.atEnd()) {
			sweeper.next(params);
//			System.out.println("x: " + params.getValue("x") + ", y: " + params.getValue("y"));
//			System.out.println(params.getValue("name") + ", " 
//					+ params.getValue("doubleList") + ", " + params.getValue("doubleRange"));
			runResultAction.runCleanup(runState, MASTER_ID);
		}
		assertEquals(3, params.getValue("x"));
	}
		
	public void testHillMultipleHill2() {
		System.out.println("testHillMultipleHill2");
		
		sweeper.setResultProducer(new RunResultProducer() {
			public double getRunValue(RunState runState) {
				Parameters params = RunEnvironment.getInstance().getParameters();
				
				double x = (Integer) params.getValue("x");
				
				// in [0, 30] has global max near x=3 and a local max near x=20, x=30 
				
				return (((((x - 10)*(x - 30))*(x - 15))*x)*(x - 25))/400; 
			}
		});
		
		RunEnvironment.init(null, null, params, false);
		
		// have to update
		ControllerAction runResultAction = runState.getControllerRegistry().getActionTree(
				MASTER_ID).getChildren(ControllerRegistry.ACTION_TREE_ROOT).iterator().next();
		// uncomment these to get a cleaner run for debugging purposes
//		sweeper.remove(intRangeInit);
//		sweeper.remove(this.doubleRangeInit);
//		sweeper.remove(this.doubleListInit);
//		sweeper.remove(this.nameInit);
		
		sweeper.setAdvancementChooser(new HillClimber());
		// find the 2nd hill
		sweeper.addToRoot(new IntSteppedSetter("x", 15, 30, 1));
		sweeper.addToRoot(new IntSteppedSetter("y", 0, 30, 1));
		RandomHelper.setSeed((int)System.currentTimeMillis());
//		Random.setSeed(1149713758950L);
		System.out.println(RandomHelper.getSeed());
		while (!sweeper.atEnd()) {
			sweeper.next(params);
//			System.out.println("x: " + params.getValue("x") + ", y: " + params.getValue("y"));
//			System.out.println(params.getValue("name") + ", " 
//					+ params.getValue("doubleList") + ", " + params.getValue("doubleRange"));
			runResultAction.runCleanup(runState, MASTER_ID);
		}
		assertEquals(20, params.getValue("x"));
	}
	
	public void testAnnealingMultipleHill() {
		System.out.println("testAnnealingMultipleHill");
		
		sweeper.setResultProducer(new RunResultProducer() {
			public double getRunValue(RunState runState) {
				Parameters params = RunEnvironment.getInstance().getParameters();
				
				double x = (Integer) params.getValue("x");
				
				// in [0, 30] has global max near x=3 and a local max near x=20, x=30 
				
				return (((((x - 10)*(x - 30))*(x - 15))*x)*(x - 25))/400; 
			}
		});
		
		RunEnvironment.init(null, null, params, false);
		
		// have to update
		ControllerAction runResultAction = runState.getControllerRegistry().getActionTree(
				MASTER_ID).getChildren(ControllerRegistry.ACTION_TREE_ROOT).iterator().next();
		// uncomment these to get a cleaner run for debugging purposes
//		sweeper.remove(intRangeInit);
//		sweeper.remove(this.doubleRangeInit);
//		sweeper.remove(this.doubleListInit);
//		sweeper.remove(this.nameInit);
		
		sweeper.setAdvancementChooser(new AnnealingAdvancementChooser());
		sweeper.addToRoot(new IntSteppedSetter("x", 0, 40, 1));
		sweeper.addToRoot(new IntSteppedSetter("y", 0, 40, 1));
		RandomHelper.setSeed((int)System.currentTimeMillis());
//		RandomHelper.setSeed(RandomHelper.DEFAULT_STREAM, 642579250L);
		System.out.println(RandomHelper.getSeed());
		while (!sweeper.atEnd()) {
			sweeper.next(params);
//			System.out.println("x: " + params.getValue("x") + ", y: " + params.getValue("y"));
//			System.out.println(params.getValue("name") + ", " 
//					+ params.getValue("doubleList") + ", " + params.getValue("doubleRange"));
			runResultAction.runCleanup(runState, MASTER_ID);
		}
		// there are 3 possible answers, accept all (in order of height: x=40, x=3, x=20)
		int xValue = (Integer) params.getValue("x");
		System.out.println(xValue);
		assertTrue(Integer.toString(xValue), xValue == 40 || xValue == 3 || xValue == 20);
	}
	
	public void testAnnealMultHillRepeatedly() throws Exception {
		for (int i = 0; i < 100; i++) {
			setUp();
			testAnnealingMultipleHill();
		}
	}
}
