package simple.chart;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;
import repast.simphony.statecharts.generator.GeneratedFor;

import simple.*;

@GeneratedFor("_2VqLcA_5EeOncZTLucYA7w")
public class SimpleStatechart extends DefaultStateChart<simple.Agent> {

	public static SimpleStatechart createStateChart(simple.Agent agent,
			double begin) {
		SimpleStatechart result = createStateChart(agent);
		StateChartScheduler.INSTANCE.scheduleBeginTime(begin, result);
		return result;
	}

	public static SimpleStatechart createStateChart(simple.Agent agent) {
		SimpleStatechartGenerator generator = new SimpleStatechartGenerator();
		return generator.build(agent);
	}

	private SimpleStatechart(simple.Agent agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends
			StateChartBuilder<simple.Agent> {

		public MyStateChartBuilder(simple.Agent agent,
				AbstractState<simple.Agent> entryState, String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
			setPriority(0.0);
		}

		@Override
		public SimpleStatechart build() {
			SimpleStatechart result = new SimpleStatechart(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class SimpleStatechartGenerator {

		private Map<String, AbstractState<Agent>> stateMap = new HashMap<String, AbstractState<Agent>>();

		public SimpleStatechart build(Agent agent) {
			SimpleStateBuilder<Agent> ssBuilder1 = new SimpleStateBuilder<Agent>(
					"State 0");
			SimpleState<Agent> s1 = ssBuilder1.build();
			stateMap.put("_AIwV0A_9EeOpYuNLQpQPLA", s1);
			MyStateChartBuilder mscb = new MyStateChartBuilder(agent, s1,
					"_AIwV0A_9EeOpYuNLQpQPLA");

			// Composite State 1
			CompositeState<Agent> cs2 = createCS2();
			mscb.addRootState(cs2, "_Ag-BgQ_9EeOpYuNLQpQPLA");

			BranchState<Agent> branch1 = new BranchStateBuilder<Agent>(
					"Choice 17").build();
			stateMap.put("_13710BmKEeOp8PBe9JU3BA", branch1);
			mscb.addRootState(branch1, "_13710BmKEeOp8PBe9JU3BA");

			FinalStateBuilder<Agent> ssBuilder3 = new FinalStateBuilder<Agent>(
					"Final State 26");
			FinalState<Agent> s3 = ssBuilder3.build();
			stateMap.put("_V_MJUBmPEeOyIYh7Z3s24Q", s3);
			mscb.addRootState(s3, "_V_MJUBmPEeOyIYh7Z3s24Q");
			SimpleStateBuilder<Agent> ssBuilder4 = new SimpleStateBuilder<Agent>(
					"State 50");
			SimpleState<Agent> s4 = ssBuilder4.build();
			stateMap.put("_odflgByhEeO8z9BKo9KXFg", s4);
			mscb.addRootState(s4, "_odflgByhEeO8z9BKo9KXFg");
			SimpleStateBuilder<Agent> ssBuilder5 = new SimpleStateBuilder<Agent>(
					"State 54");
			SimpleState<Agent> s5 = ssBuilder5.build();
			stateMap.put("_viM-YByhEeO8z9BKo9KXFg", s5);
			mscb.addRootState(s5, "_viM-YByhEeO8z9BKo9KXFg");
			createTransitions(mscb);
			return mscb.build();

		}

		// Creates CompositeState 'Composite State 1'
		private CompositeState<Agent> createCS2() {

			SimpleStateBuilder<Agent> ssBuilder6 = new SimpleStateBuilder<Agent>(
					"State 5");
			SimpleState<Agent> s6 = ssBuilder6.build();
			stateMap.put("_EKUGwQ_9EeOpYuNLQpQPLA", s6);

			CompositeStateBuilder<Agent> csBuilder2 = new CompositeStateBuilder<Agent>(
					"Composite State 1", s6, "_EKUGwQ_9EeOpYuNLQpQPLA");

			FinalStateBuilder<Agent> ssBuilder7 = new FinalStateBuilder<Agent>(
					"Final State 23");
			FinalState<Agent> s7 = ssBuilder7.build();
			stateMap.put("_R1S7wBmPEeOyIYh7Z3s24Q", s7);
			csBuilder2.addChildState(s7, "_R1S7wBmPEeOyIYh7Z3s24Q");

			HistoryStateBuilder<Agent> ssBuilder8 = new HistoryStateBuilder<Agent>(
					"Shallow History 30", true);
			HistoryState<Agent> s8 = ssBuilder8.build();
			stateMap.put("_Q1vt0BmQEeOyIYh7Z3s24Q", s8);
			csBuilder2.addHistoryState(s8, "_Q1vt0BmQEeOyIYh7Z3s24Q");

			HistoryStateBuilder<Agent> ssBuilder9 = new HistoryStateBuilder<Agent>(
					"Deep History 31", false);
			HistoryState<Agent> s9 = ssBuilder9.build();
			stateMap.put("_RSG5sRmQEeOyIYh7Z3s24Q", s9);
			csBuilder2.addHistoryState(s9, "_RSG5sRmQEeOyIYh7Z3s24Q");

			// Composite State 58
			CompositeState<Agent> cs10 = createCS10();
			csBuilder2.addChildState(cs10, "_BUxoIRyiEeO8z9BKo9KXFg");

			CompositeState<Agent> cs2 = csBuilder2.build();
			stateMap.put("_Ag-BgQ_9EeOpYuNLQpQPLA", cs2);
			return cs2;
		}

		// Creates CompositeState 'Composite State 58'
		private CompositeState<Agent> createCS10() {

			SimpleStateBuilder<Agent> ssBuilder11 = new SimpleStateBuilder<Agent>(
					"State 6");
			SimpleState<Agent> s11 = ssBuilder11.build();
			stateMap.put("_EmoPUQ_9EeOpYuNLQpQPLA", s11);

			CompositeStateBuilder<Agent> csBuilder10 = new CompositeStateBuilder<Agent>(
					"Composite State 58", s11, "_EmoPUQ_9EeOpYuNLQpQPLA");

			SimpleStateBuilder<Agent> ssBuilder12 = new SimpleStateBuilder<Agent>(
					"State 60");
			SimpleState<Agent> s12 = ssBuilder12.build();
			stateMap.put("_FEpzMRyiEeO8z9BKo9KXFg", s12);
			csBuilder10.addChildState(s12, "_FEpzMRyiEeO8z9BKo9KXFg");

			CompositeState<Agent> cs10 = csBuilder10.build();
			stateMap.put("_BUxoIRyiEeO8z9BKo9KXFg", cs10);
			return cs10;
		}

		private void createTransitions(MyStateChartBuilder mscb) {
			// creates transition Transition 3
			createTransition1(mscb);
			// creates transition Transition 8
			createTransition2(mscb);
			// creates transition Transition 14
			createTransition3(mscb);
			// creates transition Transition 18
			createTransition4(mscb);
			// creates transition Transition 21
			createTransition5(mscb);
			// creates transition Transition 27
			createTransition6(mscb);
			// creates transition Transition 34
			createTransition7(mscb);
			// creates transition Transition 35
			createTransition8(mscb);
			// creates transition Transition 51
			createTransition9(mscb);
			// creates transition Transition 55
			createTransition10(mscb);
			// creates transition Transition 61
			createTransition11(mscb);
			// creates transition Transition 62
			createTransition12(mscb);

		}

		// creates transition Transition 3, from = State 0, to = Choice 17
		private void createTransition1(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 3", stateMap.get("_AIwV0A_9EeOpYuNLQpQPLA"),
					stateMap.get("_13710BmKEeOp8PBe9JU3BA"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction1()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_CiLEQQ_9EeOpYuNLQpQPLA");
		}

		// creates transition Transition 8, from = State 5, to = Composite State 58
		private void createTransition2(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 8", stateMap.get("_EKUGwQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_BUxoIRyiEeO8z9BKo9KXFg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction2()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_GTkSQQ_9EeOpYuNLQpQPLA");
		}

		// creates transition Transition 14, from = State 6, to = State 6
		private void createTransition3(MyStateChartBuilder mscb) {
			// true
			SelfTransitionBuilder<Agent> tb = new SelfTransitionBuilder<Agent>(
					"Transition 14", stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"));
			tb.addTrigger(new AlwaysTrigger(1.0));
			tb.setPriority(0.0);
			mscb.addSelfTransition(tb.build(), "_PF8xEBTjEeOha-s4VV8EjQ");
		}

		// creates transition Transition 18, from = Choice 17, to = State 5
		private void createTransition4(MyStateChartBuilder mscb) {

			OutOfBranchTransitionBuilder<Agent> tb = new OutOfBranchTransitionBuilder<Agent>(
					"Transition 18", stateMap.get("_13710BmKEeOp8PBe9JU3BA"),
					stateMap.get("_EKUGwQ_9EeOpYuNLQpQPLA"));
			tb.addTrigger(new ConditionTrigger<Agent>(
					new SC1ConditionTriggerCondition4(), 1.0));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 21, from = Choice 17, to = Final State 23
		private void createTransition5(MyStateChartBuilder mscb) {
			DefaultOutOfBranchTransitionBuilder<Agent> tb = new DefaultOutOfBranchTransitionBuilder<Agent>(
					"Transition 21", stateMap.get("_13710BmKEeOp8PBe9JU3BA"),
					stateMap.get("_R1S7wBmPEeOyIYh7Z3s24Q"));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 27, from = State 6, to = Final State 26
		private void createTransition6(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 27", stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_V_MJUBmPEeOyIYh7Z3s24Q"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction6()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_XhGoARmPEeOyIYh7Z3s24Q");
		}

		// creates transition Transition 34, from = State 5, to = State 50
		private void createTransition7(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 34", stateMap.get("_EKUGwQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_odflgByhEeO8z9BKo9KXFg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction7()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_UTiqYBmQEeOyIYh7Z3s24Q");
		}

		// creates transition Transition 35, from = State 6, to = State 54
		private void createTransition8(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 35", stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_viM-YByhEeO8z9BKo9KXFg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction8()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_VilQYRmQEeOyIYh7Z3s24Q");
		}

		// creates transition Transition 51, from = State 50, to = Shallow History 30
		private void createTransition9(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 51", stateMap.get("_odflgByhEeO8z9BKo9KXFg"),
					stateMap.get("_Q1vt0BmQEeOyIYh7Z3s24Q"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction9()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_qqOi0ByhEeO8z9BKo9KXFg");
		}

		// creates transition Transition 55, from = State 54, to = Deep History 31
		private void createTransition10(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 55", stateMap.get("_viM-YByhEeO8z9BKo9KXFg"),
					stateMap.get("_RSG5sRmQEeOyIYh7Z3s24Q"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction10()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_yJSVYRyhEeO8z9BKo9KXFg");
		}

		// creates transition Transition 61, from = State 6, to = State 60
		private void createTransition11(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 61", stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_FEpzMRyiEeO8z9BKo9KXFg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction11()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_Gya8kByiEeO8z9BKo9KXFg");
		}

		// creates transition Transition 62, from = State 60, to = State 6
		private void createTransition12(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 62", stateMap.get("_FEpzMRyiEeO8z9BKo9KXFg"),
					stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction12()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build(), "_HPJ74ByiEeO8z9BKo9KXFg");
		}

	}
}
