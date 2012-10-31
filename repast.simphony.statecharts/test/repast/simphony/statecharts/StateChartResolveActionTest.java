package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class StateChartResolveActionTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	private static class TestStateChart extends DefaultStateChart {

		TestClass testClass;
		
		public TestStateChart(TestClass tc){
			testClass = tc;
		}
		@Override
		public void resolve() {
			testClass.resolved = true;
		}
		
	}
	
	private static class TestClass {
		public boolean resolved;
	}

	@Test
	public void testRegisterStateChartListener() {
		// create state chart with resolve method overridden
		StateChartResolveAction scra = new StateChartResolveAction();
		TestClass tc = new TestClass();
		assertEquals(false,tc.resolved);
		TestStateChart tsc = new TestStateChart(tc);
		assertEquals(false, tc.resolved);
		scra.registerListener(tsc);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
		schedule.schedule(ScheduleParameters.createOneTime(1), scra);
		schedule.execute();
		assertEquals(true, tc.resolved);
	}
	
	@Test
	public void testRegisterAndRemoveStateChartListener() {
		// create state chart with resolve method overridden
		StateChartResolveAction scra = new StateChartResolveAction();
		TestClass tc = new TestClass();
		assertEquals(false,tc.resolved);
		TestStateChart tsc = new TestStateChart(tc);
		assertEquals(false, tc.resolved);
		scra.registerListener(tsc);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
		schedule.schedule(ScheduleParameters.createOneTime(1), scra);
		scra.removeListener(tsc);
		schedule.execute();
		assertEquals(false, tc.resolved);
	}
	
	@Test
	public void testRegisterTwiceAndRemoveOneStateChartListener() {
		// create state chart with resolve method overridden
		StateChartResolveAction scra = new StateChartResolveAction();
		
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
		schedule.schedule(ScheduleParameters.createOneTime(1), scra);
		
		TestClass tc = new TestClass();
		assertEquals(false,tc.resolved);
		TestStateChart tsc = new TestStateChart(tc);
		assertEquals(false, tc.resolved);
		// register twice
		scra.registerListener(tsc);
		scra.registerListener(tsc);
		
		// remove once
		scra.removeListener(tsc);
		
		schedule.execute();
		assertEquals(true, tc.resolved);
	}
	
	@Test
	public void testRegisterMultipleStateChartListeners() {
		// create state chart with resolve method overridden
		StateChartResolveAction scra = new StateChartResolveAction();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule(); 
		schedule.schedule(ScheduleParameters.createOneTime(1), scra);

		TestClass tc1 = new TestClass();
		TestClass tc2 = new TestClass();
		TestStateChart tsc1 = new TestStateChart(tc1);
		TestStateChart tsc2 = new TestStateChart(tc2);
		scra.registerListener(tsc1);
		assertEquals(false, tc1.resolved);
		scra.registerListener(tsc2);
		assertEquals(false, tc2.resolved);
		
		scra.removeListener(tsc1);
		schedule.execute();
		assertEquals(false, tc1.resolved);
		assertEquals(true, tc2.resolved);
	}


}
