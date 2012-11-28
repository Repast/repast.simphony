package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

		private StateChart<MyAgent1> st;
		public TestClass tc1, tc2, tc3, tc4;

		public MyAgent1(TestClass tc1, TestClass tc2, TestClass tc3,
				TestClass tc4) {
			this.tc1 = tc1;
			this.tc2 = tc2;
			this.tc3 = tc3;
			this.tc4 = tc4;
		}

		@SuppressWarnings("unused")
		public void setup() {
			st = new MyStateChart1(this, tc1, tc2, tc3, tc4);
			st.begin();
		}
	}

	private static class MyStateChart1 extends DefaultStateChart<MyAgent1> {
		public MyStateChart1(MyAgent1 agent, TestClass tc1, TestClass tc2,
				TestClass tc3, TestClass tc4) {
			super(agent);
			MyState one = new MyState("one", tc1);
			this.registerEntryState(one);
			MyState two = new MyState("two", tc2);
			Trigger tr1 = new TimedTrigger(2);
			Transition<MyAgent1> t1 = new MyTransition(tr1, one, two, tc3);
			this.addRegularTransition(t1);
			Trigger tr2 = new TimedTrigger(2);
			Transition<MyAgent1> t2 = new MyTransition(tr2, two, one, tc4);
			this.addRegularTransition(t2);
		}
	}

	private static class MyState extends SimpleState<MyAgent1> {

		public TestClass tc;

		public MyState(String id, TestClass tc) {
			super(id);
			this.tc = tc;
			registerOnEnter(new StateAction<MyAgent1>() {

				@Override
				public void action(MyAgent1 agent, AbstractState<MyAgent1> state)
						throws Exception {
					MyState.this.tc.onEnter = true;

				}
			});
			//
			// register on exit
			registerOnExit(new StateAction<MyAgent1>() {

				@Override
				public void action(MyAgent1 agent, AbstractState<MyAgent1> state)
						throws Exception {
					MyState.this.tc.onExit = true;
				}
			});
		}

	}

	private static class MyTransition extends Transition<MyAgent1> {

		public final TestClass tc;

		public MyTransition(Trigger trigger, AbstractState<MyAgent1> source,
				AbstractState<MyAgent1> target, TestClass tc) {
			super(trigger, source, target);
			this.tc = tc;
			registerGuard(TransitionBuilder.<MyAgent1> createEmptyGuard());
			registerOnTransition(new TransitionAction<MyAgent1>() {

				@Override
				public void action(MyAgent1 agent,
						Transition<MyAgent1> transition) throws Exception {
					MyTransition.this.tc.onEnter = true;
					MyTransition.this.tc.onExit = true;
				}
			});
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

	private static class MyStateChart2 extends DefaultStateChart<MyAgent2> {

		public MyStateChart2(MyAgent2 agent) {
			super(agent);

			SimpleState<MyAgent2> one = new SimpleStateBuilder<MyAgent2>("one")
					.build();
			this.registerEntryState(one);
			SimpleState<MyAgent2> two = new SimpleStateBuilder<MyAgent2>("two")
					.build();
			SimpleState<MyAgent2> three = new SimpleStateBuilder<MyAgent2>(
					"three").build();
			Trigger tr1 = new MessageTrigger<MyAgent2>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			TransitionBuilder<MyAgent2> tb = new TransitionBuilder<MyAgent2>(one,two);
			tb.addTrigger(tr1);
			Transition<MyAgent2> t1 = tb.build();
			this.addRegularTransition(t1);
			tb = new TransitionBuilder<MyAgent2>(two,three);
			Trigger tr2 = new MessageTrigger<MyAgent2>(getQueue(),
					new MessageEqualsMessageChecker<String>("b"));
			tb.addTrigger(tr2);
			Transition<MyAgent2> t2 = tb.build();
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent2 {

		public StateChart<MyAgent2> st;
		@SuppressWarnings("unused")
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
		StateChart<MyAgent2> st = a.st;
		st.receiveMessage("a");
		st.receiveMessage("b");
		schedule.execute();
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
	}

	@Test
	public void myStateChart2Scenario2() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart<MyAgent2> st = a.st;
		st.receiveMessage("a");
		st.receiveMessage("a");
		st.receiveMessage("b");
		schedule.execute();
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
	}

	@Test
	public void myStateChart2Scenario3_simple() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart<MyAgent2> st = a.st;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		st.receiveMessage("hello");
		schedule.execute();
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(3, schedule.getTickCount(), 0.0001);
		// the queue should have been cleared if no candidate was found in time
		// step
		assertEquals(true, ((DefaultStateChart<?>) a.st).getQueue().isEmpty());
	}

	@Test
	public void myStateChart2Scenario3() {
		MyAgent2 a = new MyAgent2();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		StateChart<MyAgent2> st = a.st;
		schedule.execute();
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(2, schedule.getTickCount(), 0.0001);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(4, schedule.getTickCount(), 0.0001);
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		st.receiveMessage("b");
		schedule.execute();
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
	}

	/**
	 * For testing transition resolution when multiple transitions are
	 * triggered.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3 extends DefaultStateChart<MyAgent3> {

		public MyStateChart3(MyAgent3 agent) {
			super(agent);
			SimpleState<MyAgent3> one = new SimpleStateBuilder<MyAgent3>("one")
					.build();
			this.registerEntryState(one);
			SimpleState<MyAgent3> two = new SimpleStateBuilder<MyAgent3>("two")
					.build();
			SimpleState<MyAgent3> three = new SimpleStateBuilder<MyAgent3>(
					"three").build();
			Trigger tr1 = new MessageTrigger<MyAgent3>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			TransitionBuilder<MyAgent3> tb = new TransitionBuilder<MyAgent3>(one,two);
			tb.addTrigger(tr1);
			tb.setPriority(1);
			Transition<MyAgent3> t1 = tb.build();
			this.addRegularTransition(t1);
			Trigger tr2 = new MessageTrigger<MyAgent3>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			tb = new TransitionBuilder<MyAgent3>(one,three);
			tb.addTrigger(tr2);
			Transition<MyAgent3> t2 = tb.build();
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent3 {

		public StateChart<MyAgent3> st;
		@SuppressWarnings("unused")
		public void setup() {
			st = new MyStateChart3(this);
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		StateChart<MyAgent3> st = a.st;
		((DefaultStateChart<MyAgent3>) st)
				.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		StateChart<MyAgent3> st = a.st;
		((DefaultStateChart<MyAgent3>) st)
				.setTransitionResolutionStrategy(TransitionResolutionStrategy.NATURAL);
		st.receiveMessage("a");
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		StateChart<MyAgent3> st = a.st;
		((DefaultStateChart<MyAgent3>) st)
				.setTransitionResolutionStrategy(TransitionResolutionStrategy.RANDOM);
		st.receiveMessage("a");
		// RandomHelper.getDefaultRegistry().setSeed(0);
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
	}

	/**
	 * For testing transition deactivation.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart4 extends DefaultStateChart<MyAgent4> {

		public MyStateChart4(MyAgent4 agent) {
			super(agent);
			SimpleState<MyAgent4> one = new SimpleStateBuilder<MyAgent4>("one")
					.build();
			this.registerEntryState(one);
			SimpleState<MyAgent4> two = new SimpleStateBuilder<MyAgent4>("two")
					.build();
			SimpleState<MyAgent4> three = new SimpleStateBuilder<MyAgent4>(
					"three").build();
			Trigger tr1 = new TimedTrigger(2);
			TransitionBuilder<MyAgent4> tb = new TransitionBuilder<MyAgent4>(one,two);
			tb.addTrigger(tr1);
			tb.setPriority(1);
			Transition<MyAgent4> t1 = tb.build();
			this.addRegularTransition(t1);
			Trigger tr2 = new TimedTrigger(3);
			tb = new TransitionBuilder<MyAgent4>(one,three);
			tb.addTrigger(tr2);
			Transition<MyAgent4> t2 = tb.build();
			this.addRegularTransition(t2);
		}
	}

	private static class MyAgent4 {

		public StateChart<MyAgent4> st;
		@SuppressWarnings("unused")
		public void setup() {
			st = new MyStateChart4(this);
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());

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
		assertEquals("two", a.st.getCurrentSimpleState().getId());
	}

	/**
	 * For testing rescheduling recurring transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart5 extends DefaultStateChart<MyAgent5> {

		public MyStateChart5(final MyAgent5 a) {
			super(a);
			SimpleState<MyAgent5> one = new SimpleStateBuilder<MyAgent5>("one")
					.build();
			this.registerEntryState(one);
			SimpleState<MyAgent5> two = new SimpleStateBuilder<MyAgent5>("two")
					.build();
			Trigger tr1 = new ConditionTrigger<MyAgent5>(
					new ConditionTriggerCondition<MyAgent5>() {

						@Override
						public boolean condition(MyAgent5 agent,
								Transition<MyAgent5> transition)
								throws Exception {
							return a.isTrue;
						}

					}, 2);
			TransitionBuilder<MyAgent5> tb = new TransitionBuilder<MyAgent5>(one,two);
			tb.addTrigger(tr1);
			tb.setPriority(1);
			Transition<MyAgent5> t1 = tb.build();
			this.addRegularTransition(t1);
		}
	}

	private static class MyAgent5 {

		public boolean isTrue = false;
		public StateChart<MyAgent5> st;
		@SuppressWarnings("unused")
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		a.isTrue = true;
		schedule.execute();
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());

	}

	/**
	 * For testing branching transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart6 extends DefaultStateChart<MyAgent6> {
		public static StateChart<MyAgent6> createStateChart(MyAgent6 agent){
			SimpleState<MyAgent6> one = new SimpleStateBuilder<MyAgent6>("one")
					.build();
			SimpleState<MyAgent6> two = new SimpleStateBuilder<MyAgent6>("two")
					.build();
			SimpleState<MyAgent6> three = new SimpleStateBuilder<MyAgent6>(
					"three").build();
			SimpleState<MyAgent6> four = new SimpleStateBuilder<MyAgent6>(
					"four").build();

			ConditionTriggerCondition<MyAgent6> state2Condition = new ConditionTriggerCondition<MyAgent6>() {
				@Override
				public boolean condition(MyAgent6 agent,
						Transition<MyAgent6> transition) {
					return agent.value > 0.75;
				}
			};
			ConditionTriggerCondition<MyAgent6> state3Condition = new ConditionTriggerCondition<MyAgent6>() {

				@Override
				public boolean condition(MyAgent6 agent,
						Transition<MyAgent6> transition) {
					return agent.value > 0.25;
				}

			};

			StateChartBuilder<MyAgent6> scb = new StateChartBuilder<StateChartTest.MyAgent6>(agent, one);
			BranchState<MyAgent6> bs = new BranchStateBuilder<StateChartTest.MyAgent6>("branch").build();
			scb.addRootState(bs);
			scb.addRootState(two);
			scb.addRootState(three);
			scb.addRootState(four);
			
			TransitionBuilder<MyAgent6> tb = new TransitionBuilder<StateChartTest.MyAgent6>(one,bs);
			tb.addTrigger(new TimedTrigger(1));
			scb.addRegularTransition(tb.build());
			
			OutOfBranchTransitionBuilder<MyAgent6> oobtb = new OutOfBranchTransitionBuilder<MyAgent6>(bs,two);
			oobtb.addTrigger(new ConditionTrigger<MyAgent6>(state2Condition));
			oobtb.setPriority(2);
			Transition<MyAgent6> bt = oobtb.build();
			scb.addRegularTransition(bt);
			
			oobtb = new OutOfBranchTransitionBuilder<MyAgent6>(bs,three);
			oobtb.addTrigger(new ConditionTrigger<MyAgent6>(state3Condition));
			oobtb.setPriority(1);
			bt = oobtb.build();
			scb.addRegularTransition(bt);
			
			
			DefaultOutOfBranchTransitionBuilder<MyAgent6> doobtb = new DefaultOutOfBranchTransitionBuilder<MyAgent6>(bs,four);
			bt = doobtb.build();
			scb.addRegularTransition(bt);
			return scb.build();
		}
		private MyStateChart6(MyAgent6 agent) {
			super(agent);
		}
	}

	private static class MyAgent6 {

		public double value;
		public StateChart<MyAgent6> st;
		@SuppressWarnings("unused")
		public void setup() {
			st = MyStateChart6.createStateChart(this);
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.1;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentSimpleState().getId());
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.8;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.6;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}
	
	/**
	 * For testing branching transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart6b extends DefaultStateChart<MyAgent6b> {
		public static StateChart<MyAgent6b> createStateChart(MyAgent6b agent){
			SimpleState<MyAgent6b> one = new SimpleStateBuilder<MyAgent6b>("one")
					.build();
			SimpleState<MyAgent6b> two = new SimpleStateBuilder<MyAgent6b>("two")
					.build();
			SimpleState<MyAgent6b> three = new SimpleStateBuilder<MyAgent6b>(
					"three").build();
			SimpleState<MyAgent6b> four = new SimpleStateBuilder<MyAgent6b>(
					"four").build();

			ConditionTriggerCondition<MyAgent6b> state2Condition = new ConditionTriggerCondition<MyAgent6b>() {
				@Override
				public boolean condition(MyAgent6b agent,
						Transition<MyAgent6b> transition) {
					return agent.value > 0.75;
				}
			};
			ConditionTriggerCondition<MyAgent6b> state3Condition = new ConditionTriggerCondition<MyAgent6b>() {

				@Override
				public boolean condition(MyAgent6b agent,
						Transition<MyAgent6b> transition) {
					return agent.value > 0.25;
				}

			};
			BranchState<MyAgent6b> bs = new BranchStateBuilder<StateChartTest.MyAgent6b>("branch").build();
			CompositeStateBuilder<MyAgent6b> zeroBuilder = new CompositeStateBuilder<MyAgent6b>("zero",one);
			zeroBuilder.addChildState(bs);
			zeroBuilder.addChildState(two);
			CompositeState<MyAgent6b> zero = zeroBuilder.build();

			StateChartBuilder<MyAgent6b> scb = new StateChartBuilder<StateChartTest.MyAgent6b>(agent, zero);
			scb.addRootState(three);
			scb.addRootState(four);
			
			TransitionBuilder<MyAgent6b> tb = new TransitionBuilder<StateChartTest.MyAgent6b>(one,bs);
			tb.addTrigger(new TimedTrigger(1));
			scb.addRegularTransition(tb.build());
			
			OutOfBranchTransitionBuilder<MyAgent6b> oobtb = new OutOfBranchTransitionBuilder<MyAgent6b>(bs,two);
			oobtb.addTrigger(new ConditionTrigger<MyAgent6b>(state2Condition));
			oobtb.setPriority(2);
			Transition<MyAgent6b> bt = oobtb.build();
			scb.addRegularTransition(bt);
			
			oobtb = new OutOfBranchTransitionBuilder<MyAgent6b>(bs,three);
			oobtb.addTrigger(new ConditionTrigger<MyAgent6b>(state3Condition));
			oobtb.setPriority(1);
			bt = oobtb.build();
			scb.addRegularTransition(bt);
			
			
			DefaultOutOfBranchTransitionBuilder<MyAgent6b> doobtb = new DefaultOutOfBranchTransitionBuilder<MyAgent6b>(bs,four);
			bt = doobtb.build();
			scb.addRegularTransition(bt);
			return scb.build();
		}
		private MyStateChart6b(MyAgent6b agent) {
			super(agent);
		}
	}

	private static class MyAgent6b {

		public double value;
		public StateChart<MyAgent6b> st;
		@SuppressWarnings("unused")
		public void setup() {
			st = MyStateChart6b.createStateChart(this);
			st.begin();
		}
	}

	/**
	 * Should go to default.
	 */
	@Test
	public void myStateChart6bScenario1() {
		MyAgent6b a = new MyAgent6b();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.1;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * Should go to two.
	 */
	@Test
	public void myStateChart6bScenario2() {
		MyAgent6b a = new MyAgent6b();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.8;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * Should go to three.
	 */
	@Test
	public void myStateChart6bScenario3() {
		MyAgent6b a = new MyAgent6b();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
		a.value = 0.6;
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(TransitionResolutionStrategy.RANDOM,
				a.st.getTransitionResolutionStrategy());
	}

	/**
	 * For testing self transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart7 extends DefaultStateChart<MyAgent7> {

		public MyStateChart7(final MyAgent7 a) {
			super(a);
			SimpleState<MyAgent7> one = new SimpleStateBuilder<MyAgent7>("one").build();
			this.registerEntryState(one);
			Trigger timedTrigger = new TimedTrigger(2);
			TransitionAction<MyAgent7> onTransition =new TransitionAction<MyAgent7>(){

				@Override
				public void action(MyAgent7 agent,
						Transition<MyAgent7> transition) throws Exception {
					agent.value++;
				}
				
			};
			SelfTransitionBuilder<MyAgent7> stb = new SelfTransitionBuilder<StateChartTest.MyAgent7>(one);
			stb.addTrigger(timedTrigger);
			stb.registerOnTransition(onTransition);
			addSelfTransition(stb.build());
			Trigger messageTriggerA = new MessageTrigger<MyAgent7>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			stb = new SelfTransitionBuilder<StateChartTest.MyAgent7>(one);
			stb.addTrigger(messageTriggerA);
			stb.registerOnTransition(onTransition);
			addSelfTransition(stb.build());
			
			SimpleState<MyAgent7> two = new SimpleStateBuilder<MyAgent7>("two").build();
			Trigger messageTriggerB = new MessageTrigger<MyAgent7>(getQueue(),
					new MessageEqualsMessageChecker<String>("b"));
			TransitionBuilder<MyAgent7> tb = new TransitionBuilder<MyAgent7>(one,two);
			tb.addTrigger(messageTriggerB);
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent7 {

		public int value;
		public StateChart<MyAgent7> st;
		@SuppressWarnings("unused")
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		// another timed self transition should have occurred
		assertEquals(7, schedule.getTickCount(), 0.0001);
		assertEquals(4, a.value);
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(8, schedule.getTickCount(), 0.0001);
		assertEquals(4, a.value);
		assertEquals("two", a.st.getCurrentSimpleState().getId());

	}

	/**
	 * Alternate statechart for self transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart7b extends DefaultStateChart<MyAgent7b> {

		public MyStateChart7b(final MyAgent7b a) {
			super(a);
			SimpleState<MyAgent7b> one = new SimpleStateBuilder<MyAgent7b>("one").build();
			this.registerEntryState(one);
			// Strategy is to create non-commutative operations
			// onTransition1 is adding 1
			TransitionAction<MyAgent7b> onTransition1 = new TransitionAction<StateChartTest.MyAgent7b>() {
				
				@Override
				public void action(MyAgent7b agent, Transition<MyAgent7b> transition)
						throws Exception {
					agent.value++;
				}
			}; 

			Trigger messageTriggerA1 = new MessageTrigger<MyAgent7b>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			SelfTransitionBuilder<MyAgent7b> stb = new SelfTransitionBuilder<StateChartTest.MyAgent7b>(one);
			stb.addTrigger(messageTriggerA1);
			stb.registerOnTransition(onTransition1);
			addSelfTransition(stb.build());

			SimpleState<MyAgent7b> two = new SimpleStateBuilder<MyAgent7b>("two").build();
			Trigger messageTriggerA2 = new MessageTrigger<MyAgent7b>(getQueue(),
					new MessageEqualsMessageChecker<String>("a"));
			TransitionBuilder<MyAgent7b> tb = new TransitionBuilder<StateChartTest.MyAgent7b>(one,two);
			tb.addTrigger(messageTriggerA2);
			// onTransition2 is multiplying by 2
			TransitionAction<MyAgent7b> onTransition2 = new TransitionAction<MyAgent7b>() {

				@Override
				public void action(MyAgent7b agent, Transition<MyAgent7b> transition)
						throws Exception {
					agent.value *= 2;
				}
			};			

			tb.registerOnTransition(onTransition2);
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent7b {

		public int value;
		public StateChart<MyAgent7b> st;
		@SuppressWarnings("unused")
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
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
		assertEquals("two", a.st.getCurrentSimpleState().getId());
	}

}
