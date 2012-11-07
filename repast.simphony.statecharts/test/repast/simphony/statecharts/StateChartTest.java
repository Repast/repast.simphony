package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class StateChartTest {

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
	}

	@Test
	public void removeTime() {
		List<Double> times = new ArrayList<Double>();
		double a = 1.2, b = 2.3;
		times.add(a);
		times.add(b);
		assertEquals(2, times.size());
		times.remove(2.3);
		assertEquals(1, times.size());
		// Within double precision
		assertEquals(false, times.remove(1.200000000000001));
		assertEquals(1, times.size());
		// Beyond double precision
		assertEquals(true, times.remove(1.20000000000000001));
		assertEquals(0, times.size());
	}

	private static class MyAgent1 {

		private StateChart st;
		public TestClass tc1, tc2, tc3, tc4;

		public MyAgent1(TestClass tc1, TestClass tc2, TestClass tc3,
				TestClass tc4) {
			this.tc1 = tc1;
			this.tc2 = tc2;
			this.tc3 = tc3;
			this.tc4 = tc4;
		}

		public void setup() {
			st = new MyStateChart1(tc1, tc2, tc3, tc4);
			st.begin();
		}
	}

	private static class MyStateChart1 extends DefaultStateChart {
		public MyStateChart1(TestClass tc1, TestClass tc2, TestClass tc3,
				TestClass tc4) {
			MyState one = new MyState("one", tc1);
			this.registerEntryState(one);
			MyState two = new MyState("two", tc2);
			Trigger tr1 = new TimedTrigger(2);
			Transition t1 = new MyTransition(tr1, one, two, tc3);
			this.addRegularTransition(t1);
			Trigger tr2 = new TimedTrigger(2);
			Transition t2 = new MyTransition(tr2, two, one, tc4);
			this.addRegularTransition(t2);
		}
	}

	private static class MyState extends DefaultState {

		public TestClass tc;

		public MyState(String id, TestClass tc) {
			super(id);
			this.tc = tc;
			registerOnEnter(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					MyState.this.tc.onEnter = true;
					return null;
				}
			});

			// register on exit
			registerOnExit(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					MyState.this.tc.onExit = true;
					return null;
				}
			});
		}

	}

	private static class MyTransition extends Transition {

		public TestClass tc;

		public MyTransition(Trigger trigger, State source, State target,
				TestClass tc) {
			super(trigger, source, target);
			this.tc = tc;
		}

		@Override
		public void onTransition() {
			tc.onEnter = true;
			tc.onExit = true;
		}

	}

	private static class TestClass {
		public boolean onEnter, onExit;
	}

	/**
	 * Simple test with two states and two timed transitions between them
	 */
	@Test
	public void defaultStateChartTest1() {
		TestClass tc1 = new TestClass();// state one
		TestClass tc2 = new TestClass();// state two
		TestClass tc3 = new TestClass();// transition one (one -> two)
		TestClass tc4 = new TestClass();// transition two (two -> one)
		MyAgent1 a = new MyAgent1(tc1, tc2, tc3, tc4);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		assertEquals(false, tc1.onEnter);
		assertEquals(false, tc1.onExit);
		assertEquals(false, tc2.onEnter);
		assertEquals(false, tc2.onExit);
		assertEquals(false, tc3.onEnter);
		assertEquals(false, tc4.onEnter);
		schedule.execute();
		assertEquals(true, tc1.onEnter);
		assertEquals(false, tc1.onExit);
		assertEquals(false, tc2.onEnter);
		assertEquals(false, tc2.onExit);
		assertEquals(false, tc3.onEnter);
		assertEquals(false, tc4.onEnter);
		assertEquals(1, schedule.getTickCount(), 0.001);
		schedule.execute();
		assertEquals(true, tc1.onEnter);
		assertEquals(true, tc1.onExit);
		assertEquals(true, tc2.onEnter);
		assertEquals(false, tc2.onExit);
		assertEquals(true, tc3.onEnter);
		assertEquals(false, tc4.onEnter);
		assertEquals(3, schedule.getTickCount(), 0.001);
		tc1.onEnter = false;
		tc1.onExit = false;
		assertEquals(3, schedule.getTickCount(), 0.001);
		schedule.execute();
		assertEquals(true, tc1.onEnter);
		assertEquals(false, tc1.onExit);
		assertEquals(true, tc2.onEnter);
		assertEquals(true, tc2.onExit);
		assertEquals(true, tc3.onEnter);
		assertEquals(true, tc4.onEnter);
		assertEquals(5, schedule.getTickCount(), 0.001);
	}
	
	private static class MyStateChart2 extends DefaultStateChart {
		
		public MyStateChart2() {
			DefaultState one = new DefaultState("one");
			this.registerEntryState(one);
			DefaultState two = new DefaultState("two");
			DefaultState three = new DefaultState("three");
			Trigger tr1 = new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("a"));
			Transition t1 = new Transition(tr1, one, two);
			this.addRegularTransition(t1);
			Trigger tr2 = new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("b"));
			Transition t2 = new Transition(tr2, two, three);
			this.addRegularTransition(t2);
		}
	}
	
	private static class MyAgent2 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart2();
			st.receiveMessage("a");
			st.receiveMessage("a");
			st.receiveMessage("b");
			st.begin();
		}
	}
	
	@Test
	public void stateChartScenario1(){
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals("three",a.st.getCurrentState().getId());
	}

}
