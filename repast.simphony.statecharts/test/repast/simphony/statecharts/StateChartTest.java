package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

	private static class MyAgent {

		private StateChart st;
		public TestClass tc1, tc2, tc3, tc4;

		public MyAgent(TestClass tc1, TestClass tc2, TestClass tc3, TestClass tc4) {
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
		public MyStateChart1(TestClass tc1, TestClass tc2, TestClass tc3, TestClass tc4) {
			MyState one = new MyState("one", tc1);
			this.registerEntryState(one);
			MyState two = new MyState("two", tc2);
			Trigger tr1 = new TimedTrigger(2);
			Transition t1 = new MyTransition(tr1,one,two,tc3);
			this.addRegularTransition(t1);
			Trigger tr2 = new TimedTrigger(2);
			Transition t2 = new MyTransition(tr2,two,one,tc4);
			this.addRegularTransition(t2);
		}
	}

	private static class MyState implements State {

		private String id;
		public TestClass tc;

		public MyState(String id, TestClass tc) {
			this.id = id;
			this.tc = tc;
		}

		@Override
		public String getId() {
			return id;
		}

		@Override
		public void setId(String id) {
			this.id = id;
		}

		@Override
		public void onEnter() {
			tc.onEnter = true;
		}

		@Override
		public void onExit() {
			tc.onExit = true;
		}

	}
	
	private static class MyTransition extends Transition{
		
		public TestClass tc;

		public MyTransition(Trigger trigger, State source, State target, TestClass tc) {
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
		MyAgent a = new MyAgent(tc1,tc2,tc3,tc4);
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
		assertEquals(1,schedule.getTickCount(),0.001);
		schedule.execute();
		assertEquals(true, tc1.onEnter);
		assertEquals(true, tc1.onExit);
		assertEquals(true, tc2.onEnter);
		assertEquals(false, tc2.onExit);
		assertEquals(true, tc3.onEnter);
		assertEquals(false, tc4.onEnter);
		tc1.onEnter = false;
		tc1.onExit = false;
		assertEquals(3,schedule.getTickCount(),0.001);
		schedule.execute();
		assertEquals(true, tc1.onEnter);
		assertEquals(false, tc1.onExit);
		assertEquals(true, tc2.onEnter);
		assertEquals(true, tc2.onExit);
		assertEquals(true, tc3.onEnter);
		assertEquals(true, tc4.onEnter);
		assertEquals(5,schedule.getTickCount(),0.001);
	}

}
