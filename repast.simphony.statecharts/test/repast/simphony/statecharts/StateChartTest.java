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

public class StateChartTest {

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
		StateChartResolveActionScheduler.INSTANCE.initialize();
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

	private static class MyState extends AbstractState {

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

		public MyTransition(Trigger trigger, AbstractState source, AbstractState target,
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
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			Trigger tr1 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			Transition t1 = new Transition(tr1, one, two);
			this.addRegularTransition(t1);
			Trigger tr2 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("b"));
			Transition t2 = new Transition(tr2, two, three);
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent2 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart2();
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
		StateChart st = a.st;
		st.receiveMessage("a");
		st.receiveMessage("b");
		schedule.execute();
		assertEquals("three", a.st.getCurrentState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
	}

	@Test
	public void myStateChart2Scenario2() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart st = a.st;
		st.receiveMessage("a");
		st.receiveMessage("a");
		st.receiveMessage("b");
		schedule.execute();
		assertEquals("three", a.st.getCurrentState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
	}

	@Test
	public void myStateChart2Scenario3_simple() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart st = a.st;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		st.receiveMessage("hello");
		schedule.execute();
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(3, schedule.getTickCount(), 0.0001);
		// the queue should have been cleared if no candidate was found in time
		// step
		assertEquals(true, a.st.getQueue().isEmpty());
	}

	@Test
	public void myStateChart2Scenario3() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart st = a.st;
		schedule.execute();
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		schedule.execute();
		assertEquals(4, schedule.getTickCount(), 0.0001);
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		st.receiveMessage("b");
		schedule.execute();
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentState().getId());
	}

	/**
	 * For testing transition resolution when multiple transitions are
	 * triggered.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3 extends DefaultStateChart {

		public MyStateChart3() {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			Trigger tr1 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			Transition t1 = new Transition(tr1, one, two, 1);
			this.addRegularTransition(t1);
			Trigger tr2 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			Transition t2 = new Transition(tr2, one, three);
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent3 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart3();
			st.begin();
		}
	}

	/**
	 * Transition leading to state "two" is a higher priority.
	 */
	@Test
	public void myStateChart3Scenario1() {
		MyAgent3 a = new MyAgent3();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		StateChart st = a.st;
		st.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
	}

	/**
	 * Natural ordering of transitions (based on the order in which they were
	 * added to statechart).
	 */
	@Test
	public void myStateChart3Scenario2() {
		MyAgent3 a = new MyAgent3();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		StateChart st = a.st;
		st.setTransitionResolutionStrategy(TransitionResolutionStrategy.NATURAL);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
	}

	/**
	 * Random ordering of transitions. Generally ignored, but can be used to
	 * check results with specific seed.
	 */
	@Ignore
	@Test
	public void myStateChart3Scenario3() {
		MyAgent3 a = new MyAgent3();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		StateChart st = a.st;
		st.setTransitionResolutionStrategy(TransitionResolutionStrategy.RANDOM);
		st.receiveMessage("a");
		// RandomHelper.getDefaultRegistry().setSeed(0);
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentState().getId());
	}

	/**
	 * For testing transition deactivation.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart4 extends DefaultStateChart {

		public MyStateChart4() {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			Trigger tr1 = new TimedTrigger(2);
			Transition t1 = new Transition(tr1, one, two, 1);
			this.addRegularTransition(t1);
			Trigger tr2 = new TimedTrigger(3);
			;
			Transition t2 = new Transition(tr2, one, three);
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent4 {

		public StateChart st;

		public void setup() {
			st = new MyStateChart4();
			st.begin();
		}
	}

	/**
	 * Random ordering of transitions.
	 */
	@Test
	public void myStateChart4Scenario1() {
		MyAgent4 a = new MyAgent4();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());

		assertEquals(2,
				StateChartResolveActionScheduler.INSTANCE.resolveActions.size());
		assertEquals(true,
				StateChartResolveActionScheduler.INSTANCE.resolveActions
						.containsKey(3d));
		assertEquals(true,
				StateChartResolveActionScheduler.INSTANCE.resolveActions
						.containsKey(4d));
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		assertEquals(true,
				StateChartResolveActionScheduler.INSTANCE.resolveActions
						.isEmpty());
	}

	/**
	 * For testing rescheduling recurring transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart5 extends DefaultStateChart {

		public MyStateChart5(final MyAgent5 a) {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			AbstractState two = new SimpleState("two");
			Trigger tr1 = new ConditionTrigger(new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return a.isTrue;
				}

			}, 2);
			Transition t1 = new Transition(tr1, one, two, 1);
			this.addRegularTransition(t1);
		}
	}

	private static class MyAgent5 {

		public boolean isTrue = false;
		public StateChart st;

		public void setup() {
			st = new MyStateChart5(this);
			st.begin();
		}
	}

	/**
	 * Random ordering of transitions.
	 */
	@Test
	public void myStateChart5Scenario1() {
		MyAgent5 a = new MyAgent5();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		a.isTrue = true;
		schedule.execute();
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		assertEquals(true,
				StateChartResolveActionScheduler.INSTANCE.resolveActions
						.isEmpty());
	}

	/**
	 * For testing branching transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart6 extends DefaultStateChart {

		public MyStateChart6(final MyAgent6 a) {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			AbstractState two = new SimpleState("two");
			AbstractState three = new SimpleState("three");
			AbstractState four = new SimpleState("four");

			Callable<Boolean> state2Condition = new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return a.value > 0.75;
				}

			};
			Callable<Boolean> state3Condition = new Callable<Boolean>() {

				@Override
				public Boolean call() throws Exception {
					return a.value > 0.25;
				}

			};
			
			IntoBranchTransition ibt = IntoBranchTransition.createIntoBranchTransition(one, new TimedTrigger(1));

			List<OutOfBranchTransition> oobts = new ArrayList<OutOfBranchTransition>();
			oobts.add(OutOfBranchTransition.createOutOfBranchTransition(two,
					state2Condition));
			oobts.add(OutOfBranchTransition.createOutOfBranchTransition(three,
					state3Condition));
			oobts.add(OutOfBranchTransition.createDefaultOutOfBranchTransition(four));

			Branch branch = Branch.createBranch("branch", ibt, oobts);
			addBranch(branch);
		}
	}

	private static class MyAgent6 {

		public double value;
		public StateChart st;

		public void setup() {
			st = new MyStateChart6(this);
			st.begin();
		}
	}

	/**
	 * Should go to default.
	 */
	@Test
	public void myStateChart6Scenario1() {
		MyAgent6 a = new MyAgent6();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.1;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * Should go to two.
	 */
	@Test
	public void myStateChart6Scenario2() {
		MyAgent6 a = new MyAgent6();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.8;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * Should go to three.
	 */
	@Test
	public void myStateChart6Scenario3() {
		MyAgent6 a = new MyAgent6();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.6;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * For testing self transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart7 extends DefaultStateChart {

		public MyStateChart7(final MyAgent7 a) {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			Trigger timedTrigger = new TimedTrigger(2);
			Callable<Void> onTransition = new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					a.value++;
					return null;
				}

			};
			addSelfTransition(timedTrigger, onTransition,
					Transition.createEmptyGuard(), one);
			Trigger messageTriggerA = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			addSelfTransition(messageTriggerA, onTransition,
					Transition.createEmptyGuard(), one);

			AbstractState two = new SimpleState("two");
			Trigger messageTriggerB = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("b"));
			Transition t1 = new Transition(messageTriggerB, one, two);
			addRegularTransition(t1);
		}
	}

	private static class MyAgent7 {

		public int value;
		public StateChart st;

		public void setup() {
			st = new MyStateChart7(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart7Scenario1() {
		MyAgent7 a = new MyAgent7();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(0, a.value);
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		schedule.execute();
		// timed self transition should have occurred
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals(1, a.value);
		schedule.execute();
		assertEquals(4, schedule.getTickCount(), 0.0001);
		assertEquals(1, a.value);
		schedule.execute();
		// another timed self transition should have occurred
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals(2, a.value);
		a.st.receiveMessage("a");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals(3, a.value);
		assertEquals("one", a.st.getCurrentState().getId());
		schedule.execute();
		// another timed self transition should have occurred
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals(4, a.value);
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(8, schedule.getTickCount(), 0.0001);
		assertEquals(4, a.value);
		assertEquals("two", a.st.getCurrentState().getId());

	}

	/**
	 * Alternate statechart for self transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart7b extends DefaultStateChart {

		public MyStateChart7b(final MyAgent7b a) {
			AbstractState one = new SimpleState("one");
			this.registerEntryState(one);
			// Strategy is to create non-commutative operations
			// onTransition1 is adding 1
			Callable<Void> onTransition1 = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					a.value++;
					return null;
				}
			};

			Trigger messageTriggerA1 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			addSelfTransition(messageTriggerA1, onTransition1,
					Transition.createEmptyGuard(), one);

			AbstractState two = new SimpleState("two");
			Trigger messageTriggerA2 = new MessageTrigger(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			Transition t1 = new Transition(messageTriggerA2, one, two);
			// onTransition2 is multiplying by 2
			Callable<Void> onTransition2 = new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					a.value *= 2;
					return null;
				}
			};

			t1.registerOnTransition(onTransition2);
			addRegularTransition(t1);
		}
	}

	private static class MyAgent7b {

		public int value;
		public StateChart st;

		public void setup() {
			st = new MyStateChart7b(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart7bScenario1() {
		MyAgent7b a = new MyAgent7b();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentState().getId());
		assertEquals(0, a.value);
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals(0, a.value);
		a.st.receiveMessage("a");
		schedule.execute();
		assertEquals(4, schedule.getTickCount(), 0.0001);
		// (0 + 1) * 2 = 2 but (0 * 2) + 1 = 1 so this would indicate that the
		// addition in the self transition is completed first
		assertEquals(2, a.value);
		assertEquals("two", a.st.getCurrentState().getId());
	}

}
