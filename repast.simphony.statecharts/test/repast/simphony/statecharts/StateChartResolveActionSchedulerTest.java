package repast.simphony.statecharts;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class StateChartResolveActionSchedulerTest {

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

	@Test
	public void testSimpleAddRemove() {
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		ISchedulableAction ia = RunEnvironment.getInstance().getCurrentSchedule().schedule(ScheduleParameters.createOneTime(1), new IAction(){

			@Override
			public void execute() {
				System.out.println("Executing.");
			}});
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		assertTrue(RunEnvironment.getInstance().getCurrentSchedule().removeAction(ia));
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
	}

	
	@Test
	public void testOneStateChartSameTime() {
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChart sc = new DefaultStateChart();
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc);
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc);
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc);
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
	}
	
	@Test
	public void testTwoStateChartsSameTime() {
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChart sc1 = new DefaultStateChart();
		StateChart sc2 = new DefaultStateChart();
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc1);
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc2);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc1);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc2);
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
	}
	
	@Test
	public void testOneStateChartDifferentTime() {
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChart sc = new DefaultStateChart();
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(0.5, sc);
		assertEquals(2,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(0.5, sc);
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
	}
	
	@Test
	public void testTwoStateChartsDifferentTime() {
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChart sc1 = new DefaultStateChart();
		StateChart sc2 = new DefaultStateChart();
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(1, sc1);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(0.5, sc2);
		assertEquals(2,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(1, sc1);
		assertEquals(1,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(0.5, sc2);
		assertEquals(0,RunEnvironment.getInstance().getCurrentSchedule().getModelActionCount());
	}

}
