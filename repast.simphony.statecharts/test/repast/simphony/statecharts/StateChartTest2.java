package repast.simphony.statecharts;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;

public class StateChartTest2 {

	@Before
	public void setUp() throws Exception {
		RunEnvironment.init(new Schedule(), null, null, false);
		StateChartScheduler.INSTANCE.initialize();
	}

	/**
	 * Statechart for composite state transition not being affected by internal
	 * transition.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart1 extends DefaultStateChart<MyAgent1> {

		public MyStateChart1(MyAgent1 a) {
			super(a);
			SimpleState<MyAgent1> one = new SimpleStateBuilder<MyAgent1>("one")
					.build();
			CompositeStateBuilder<MyAgent1> csb = new CompositeStateBuilder<StateChartTest2.MyAgent1>(
					"cs",one);

			SimpleState<MyAgent1> two = new SimpleStateBuilder<MyAgent1>("two")
					.build();
			csb.addChildState(two);
			CompositeState<MyAgent1> cs = csb.build();
			this.registerEntryState(cs);
			addState(csb.build());
			SimpleState<MyAgent1> three = new SimpleStateBuilder<MyAgent1>(
					"three").build();
			addState(three);
			TransitionBuilder<MyAgent1> tb = new TransitionBuilder<StateChartTest2.MyAgent1>(one,two);
			tb.addTrigger(new TimedTrigger<MyAgent1>(1));
			addRegularTransition(tb.build());
			tb = new TransitionBuilder<StateChartTest2.MyAgent1>(cs,three);
			tb.addTrigger(new TimedTrigger<MyAgent1>(2));
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent1 {

		public StateChart<MyAgent1> st;
		@SuppressWarnings("unused")
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
	private static class MyStateChart2 extends DefaultStateChart<MyAgent2> {

		public MyStateChart2(MyAgent2 a) {
			super(a);
			SimpleState<MyAgent2> one = new SimpleStateBuilder<MyAgent2>("one")
					.build();
			CompositeStateBuilder<MyAgent2> csb = new CompositeStateBuilder<StateChartTest2.MyAgent2>(
					"cs",one);
			SimpleState<MyAgent2> two = new SimpleStateBuilder<MyAgent2>("two")
					.build();
			csb.addChildState(two);
			CompositeState<MyAgent2> cs = csb.build();
			registerEntryState(cs);
			SimpleState<MyAgent2> three = new SimpleStateBuilder<MyAgent2>(
					"three").build();

			TransitionBuilder<MyAgent2> tb = new TransitionBuilder<StateChartTest2.MyAgent2>(one,two);
			tb.addTrigger(new TimedTrigger<MyAgent2>(1));
			addRegularTransition(tb.build());

			tb = new TransitionBuilder<StateChartTest2.MyAgent2>(cs,three);
			tb.addTrigger(new TimedTrigger<MyAgent2>(2));

			addRegularTransition(tb.build());

			TransitionAction<MyAgent2> onTransition = new TransitionAction<StateChartTest2.MyAgent2>() {

				@Override
				public void action(MyAgent2 agent,
						Transition<MyAgent2> transition, Parameters params) throws Exception {
					agent.value++;

				}
			};
			SelfTransitionBuilder<MyAgent2> stb = new SelfTransitionBuilder<StateChartTest2.MyAgent2>(cs);
			stb.addTrigger(new TimedTrigger<MyAgent2>(1));
			stb.registerOnTransition(onTransition);
			addSelfTransition(stb.build());

		}
	}

	private static class MyAgent2 {

		public StateChart<MyAgent2> st;
		public int value;
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
		assertEquals("one", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		assertEquals(1, a.value);
		schedule.execute();
		assertEquals(3, schedule.getTickCount(), 0.0001);
		assertEquals("three", a.st.getCurrentSimpleState().getId());
		assertEquals(2, a.value);
	}

	/**
	 * Statechart for composite state zero time transitions.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3 extends DefaultStateChart<MyAgent3> {

		public MyStateChart3(MyAgent3 a) {
			super(a);
			SimpleState<MyAgent3> one = new SimpleStateBuilder<MyAgent3>("one")
					.build();
			CompositeStateBuilder<MyAgent3> csb = new CompositeStateBuilder<StateChartTest2.MyAgent3>(
					"cs",one);

			SimpleState<MyAgent3> two = new SimpleStateBuilder<MyAgent3>("two")
					.build();
			csb.addChildState(two);
			CompositeState<MyAgent3> cs = csb.build();
			registerEntryState(cs);

			SimpleState<MyAgent3> three = new SimpleStateBuilder<MyAgent3>(
					"three").build();

			TransitionBuilder<MyAgent3> tb = new TransitionBuilder<StateChartTest2.MyAgent3>(one,two);
			tb.addTrigger(new MessageTrigger<MyAgent3>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3,String>("a",String.class)));
			addRegularTransition(tb.build());

			tb = new TransitionBuilder<StateChartTest2.MyAgent3>(two,three);
			tb.addTrigger(new MessageTrigger<MyAgent3>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3,String>("b",String.class)));
			addRegularTransition(tb.build());
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
	private static class MyStateChart3b extends DefaultStateChart<MyAgent3b> {

		public MyStateChart3b(MyAgent3b a) {
			super(a);
			SimpleState<MyAgent3b> one = new SimpleStateBuilder<MyAgent3b>(
					"one").build();
			CompositeStateBuilder<MyAgent3b> csb = new CompositeStateBuilder<StateChartTest2.MyAgent3b>(
					"cs",one);
			SimpleState<MyAgent3b> two = new SimpleStateBuilder<MyAgent3b>(
					"two").build();
			csb.addChildState(two);
			CompositeState<MyAgent3b> cs = csb.build();
			registerEntryState(cs);

			SimpleState<MyAgent3b> three = new SimpleStateBuilder<MyAgent3b>(
					"three").build();

			TransitionBuilder<MyAgent3b> tb = new TransitionBuilder<StateChartTest2.MyAgent3b>(one,two);
			tb.addTrigger(new MessageTrigger<MyAgent3b>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3b,String>("a",String.class)));
			addRegularTransition(tb.build());

			tb = new TransitionBuilder<StateChartTest2.MyAgent3b>(cs,three);
			tb.addTrigger(new MessageTrigger<MyAgent3b>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3b,String>("b",String.class)));
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent3b {

		public StateChart<MyAgent3b> st;
		@SuppressWarnings("unused")
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
	 * Another statechart for composite state zero time transitions, but with
	 * different polling times.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart3c extends DefaultStateChart<MyAgent3c> {

		public MyStateChart3c(MyAgent3c a) {
			super(a);
			SimpleState<MyAgent3c> one = new SimpleStateBuilder<MyAgent3c>(
					"one").build();
			CompositeStateBuilder<MyAgent3c> csb = new CompositeStateBuilder<StateChartTest2.MyAgent3c>(
					"cs",one);
			
			SimpleState<MyAgent3c> two = new SimpleStateBuilder<MyAgent3c>(
					"two").build();
			csb.addChildState(two);
			CompositeState<MyAgent3c> cs = csb.build();
			registerEntryState(cs);

			SimpleState<MyAgent3c> three = new SimpleStateBuilder<MyAgent3c>(
					"three").build();

			TransitionBuilder<MyAgent3c> tb = new TransitionBuilder<StateChartTest2.MyAgent3c>(one,two);
			tb.addTrigger(new MessageTrigger<MyAgent3c>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3c,String>("a",String.class)));
			addRegularTransition(tb.build());

			tb = new TransitionBuilder<StateChartTest2.MyAgent3c>(cs,three);
			tb.addTrigger(new MessageTrigger<MyAgent3c>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent3c,String>("b",String.class), 2));
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent3c {

		public StateChart<MyAgent3c> st;
		@SuppressWarnings("unused")
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

	/**
	 * Statechart for history states.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart4 extends DefaultStateChart<MyAgent4> {

		public MyStateChart4(MyAgent4 a) {
			super(a);

			class MyHistoryStateAction implements StateAction<MyAgent4> {

				@Override
				public void action(MyAgent4 agent, AbstractState<MyAgent4> state, Parameters params)
						throws Exception {
					agent.lastHistoryDestination = ((HistoryState<MyAgent4>) state)
							.getDestination().getId();

				}
			}
			HistoryStateBuilder<MyAgent4> hsb3 = new HistoryStateBuilder<StateChartTest2.MyAgent4>(
					"hs3");
			hsb3.registerOnEnter(new MyHistoryStateAction());
			HistoryState<MyAgent4> hs3 = hsb3.build();

			HistoryStateBuilder<MyAgent4> hsb3Star = new HistoryStateBuilder<StateChartTest2.MyAgent4>(
					"hs3*",false);
			hsb3Star.registerOnEnter(new MyHistoryStateAction());
			HistoryState<MyAgent4> hs3Star = hsb3Star.build();

			HistoryStateBuilder<MyAgent4> hsb2 = new HistoryStateBuilder<StateChartTest2.MyAgent4>(
					"hs2");
			hsb2.registerOnEnter(new MyHistoryStateAction());
			HistoryState<MyAgent4> hs2 = hsb2.build();

			SimpleState<MyAgent4> four = new SimpleStateBuilder<MyAgent4>(
					"four").build();
			addState(four);

			SimpleState<MyAgent4> oneA = new SimpleStateBuilder<MyAgent4>(
					"oneA").build();
			SimpleState<MyAgent4> oneB = new SimpleStateBuilder<MyAgent4>(
					"oneB").build();
			addState(four);
			CompositeStateBuilder<MyAgent4> csb2 = new CompositeStateBuilder<StateChartTest2.MyAgent4>(
					"two",oneA);
			csb2.addChildState(oneB);
			csb2.addHistoryState(hs2);
			CompositeState<MyAgent4> cs2 = csb2.build();
			CompositeStateBuilder<MyAgent4> csb3 = new CompositeStateBuilder<StateChartTest2.MyAgent4>(
					"three",cs2);
			csb3.addHistoryState(hs3);
			csb3.addHistoryState(hs3Star);
			CompositeState<MyAgent4> cs3 = csb3.build();
			registerEntryState(cs3);

			TransitionBuilder<MyAgent4> tb = new TransitionBuilder<StateChartTest2.MyAgent4>(oneA,oneB);
			tb.addTrigger(new TimedTrigger<MyAgent4>(1));
			addRegularTransition(tb.build());
			
			tb = new TransitionBuilder<StateChartTest2.MyAgent4>(cs3,four);
			tb.addTrigger(new TimedTrigger<MyAgent4>(4));
			addRegularTransition(tb.build());
			
			tb = new TransitionBuilder<StateChartTest2.MyAgent4>(four,hs3);
			tb.addTrigger(new MessageTrigger<MyAgent4>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent4,String>("a",String.class)));
			addRegularTransition(tb.build());

			tb = new TransitionBuilder<StateChartTest2.MyAgent4>(four,hs3Star);
			tb.addTrigger(new MessageTrigger<MyAgent4>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent4,String>("b",String.class)));
			addRegularTransition(tb.build());
			
			tb = new TransitionBuilder<StateChartTest2.MyAgent4>(four,hs2);
			tb.addTrigger(new MessageTrigger<MyAgent4>(getQueue(),
					new MessageEqualsMessageChecker<MyAgent4,String>("c",String.class)));
			addRegularTransition(tb.build());
		}
	}

	private static class MyAgent4 {
		public String lastHistoryDestination;
		public StateChart<MyAgent4> st;
		@SuppressWarnings("unused")
		public void setup() {
			st = new MyStateChart4(this);
			st.begin();
		}
	}

	@Test
	public void myStateChart4Scenario1() {
		MyAgent4 a = new MyAgent4();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("oneA", a.st.getCurrentSimpleState().getId());
		assertEquals(true, a.st.withinState("three"));
		assertEquals(3, a.st.getCurrentStates().size());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("oneB", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("a");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals("oneA", a.st.getCurrentSimpleState().getId());
		assertEquals("two", a.lastHistoryDestination);

	}

	@Test
	public void myStateChart4Scenario2() {
		MyAgent4 a = new MyAgent4();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("oneA", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("oneB", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("b");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals("oneB", a.st.getCurrentSimpleState().getId());
		assertEquals("oneB", a.lastHistoryDestination);

	}

	@Test
	public void myStateChart4Scenario3() {
		MyAgent4 a = new MyAgent4();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("oneA", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("oneB", a.st.getCurrentSimpleState().getId());
		schedule.execute();
		assertEquals(5, schedule.getTickCount(), 0.0001);
		assertEquals("four", a.st.getCurrentSimpleState().getId());
		a.st.receiveMessage("c");
		schedule.execute();
		assertEquals(6, schedule.getTickCount(), 0.0001);
		assertEquals("oneB", a.st.getCurrentSimpleState().getId());
		assertEquals("oneB", a.lastHistoryDestination);

	}
	
	/**
	 * Statechart for final states.
	 * 
	 * @author jozik
	 * 
	 */
	private static class MyStateChart5 extends DefaultStateChart<MyAgent5> {
		
		public static MyStateChart5 createStateChart(MyAgent5 agent){

			SimpleState<MyAgent5> four = new SimpleStateBuilder<MyAgent5>(
					"four").build();

			SimpleState<MyAgent5> three = new SimpleStateBuilder<MyAgent5>(
					"three").build();
			
			SimpleState<MyAgent5> two = new SimpleStateBuilder<MyAgent5>(
					"two").build();
			FinalState<MyAgent5> fState = new FinalStateBuilder<MyAgent5>("final").build();
			
			CompositeStateBuilder<MyAgent5> csb1 = new CompositeStateBuilder<MyAgent5>(
					"one",two,"twotestuuid");
			csb1.addChildState(fState,"fstatetestuuid");
			
			CompositeState<MyAgent5> one = csb1.build();
			
			CompositeStateBuilder<MyAgent5> csb0 = new CompositeStateBuilder<MyAgent5>(
					"zero",one);
			
			CompositeState<MyAgent5> zero = csb0.build();
			
			MyStateChart5Builder mb = new MyStateChart5Builder(agent,zero);
			mb.addRootState(four);
			mb.addRootState(three);
			
			TransitionBuilder<MyAgent5> tb = new TransitionBuilder<MyAgent5>(two,fState);
			tb.addTrigger(new TimedTrigger<MyAgent5>(1));
			mb.addRegularTransition(tb.build());
			
			tb = new TransitionBuilder<MyAgent5>(zero,three);
			tb.addTrigger(new TimedTrigger<MyAgent5>(2));
			mb.addRegularTransition(tb.build());
			
			tb = new TransitionBuilder<MyAgent5>(one,four);
			tb.addTrigger(new MessageTrigger<MyAgent5>(new MessageEqualsMessageChecker<MyAgent5,String>("a",String.class)));
			mb.addRegularTransition(tb.build());
			
			return mb.build();
		}
		
		private static class MyStateChart5Builder extends StateChartBuilder<MyAgent5>{

			public MyStateChart5Builder(MyAgent5 agent, AbstractState<MyAgent5> entryState) {
				super(agent,entryState);
			}
			
			@Override
			public MyStateChart5 build(){
				MyStateChart5 result = new MyStateChart5(getAgent());
				setStateChartProperties(result);
				return result;
			}
		}
		
		private MyStateChart5(MyAgent5 a) {
			super(a);
		}
	}

	private static class MyAgent5 {
		public MyStateChart5 st = MyStateChart5.createStateChart(this);
		
		@SuppressWarnings("unused")
		public void setup() {
			st.begin();
		}
		
	}

	@Test
	public void myStateChart5Scenario1() {
		MyAgent5 a = new MyAgent5();
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		schedule.schedule(ScheduleParameters.createOneTime(1), a, "setup");
		schedule.execute();
		assertEquals(1, schedule.getTickCount(), 0.0001);
		assertEquals("two", a.st.getCurrentSimpleState().getId());
		assertEquals("twotestuuid", a.st.getUuidForState(a.st.getCurrentSimpleState()));
		assertEquals(false,a.st.activeRegularTransitions.isEmpty());
		assertEquals(true,StateChartScheduler.INSTANCE.resolveActions.containsKey(3d));
		schedule.execute();
		assertEquals(2, schedule.getTickCount(), 0.0001);
		assertEquals("final", a.st.getCurrentSimpleState().getId());
		assertEquals("fstatetestuuid", a.st.getUuidForState(a.st.getCurrentSimpleState()));
		assertEquals(true,a.st.activeRegularTransitions.isEmpty());
		assertEquals(false,StateChartScheduler.INSTANCE.resolveActions.containsKey(3d));
	}

}
