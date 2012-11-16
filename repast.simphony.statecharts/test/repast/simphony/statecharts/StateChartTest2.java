package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class StateChartTest2 {

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
		StateChartResolveActionScheduler.INSTANCE.initialize();
	}

	/**
	 * Statechart for composite state transition not being affected by internal
	 * transition.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart1 extends DefaultStateChart {

		public MyStateChart1(final MyAgent1 a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new TimedTrigger(1), one, two));
			addRegularTransition(new Transition(new TimedTrigger(2), cs, three));
		}
	}

	private static class MyAgent1 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart1(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart1Scenario1() {
		MyAgent1 a = new MyAgent1();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
	}

	/**
	 * Statechart for composite state transition not being affected by internal
	 * transition and self transition.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart2 extends DefaultStateChart {

		public MyStateChart2(final MyAgent2 a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new TimedTrigger(1), one, two));
			addRegularTransition(new Transition(new TimedTrigger(2), cs, three));
			Callable<Void> onTransition = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					a.value++;
					return null;
				}
			};
			addSelfTransition(new TimedTrigger(1), onTransition, Transition.createEmptyGuard(), cs);
			
		}
	}

	private static class MyAgent2 {

		public StateChart st;
		public int value;

		public void setup() {
			st = new MyStateChart2(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart2Scenario1() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		assertEquals(1,a.value);
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(2,a.value);
	}
	
	/**
	 * Statechart for composite state zero time transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3 extends DefaultStateChart {

		public MyStateChart3(final MyAgent3 a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("a")), one, two));
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("b")), two, three));
		}
	}

	private static class MyAgent3 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart3(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart3Scenario1() {
		MyAgent3 a = new MyAgent3();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("a");
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());

	}
	
	/**
	 * Another statechart for composite state zero time transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3b extends DefaultStateChart {

		public MyStateChart3b(final MyAgent3b a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("a")), one, two));
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("b")), cs, three));
		}
	}
	
	private static class MyAgent3b {

		public StateChart st;

		public void setup() {
			st = new MyStateChart3b(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart3bScenario1() {
		MyAgent3b a = new MyAgent3b();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("a");
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());

	}
	
	/**
	 * Another statechart for composite state zero time transitions, but with different polling times.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3c extends DefaultStateChart {

		public MyStateChart3c(final MyAgent3c a) {
			CompositeState cs = new CompositeState("cs");
			this.registerEntryState(cs);
			AbstractState one = new SimpleState("one");
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			cs.registerEntryState(one);
			cs.add(two);
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("a")), one, two));
			addRegularTransition(new Transition(new MessageTrigger(getQueue(), new MessageEqualsMessageChecker<String>("b"),2), cs, three));
		}
	}
	
	private static class MyAgent3c {

		public StateChart st;

		public void setup() {
			st = new MyStateChart3c(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart3cScenario1() {
		MyAgent3c a = new MyAgent3c();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("a");
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());

	}

}
