package repast.simphony.statecharts.runtime;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;

public class MyStateChart4 extends DefaultStateChart<MyAgent> {

	public static MyStateChart4 createStateChart(MyAgent agent, double begin) {
		MyStateChart4 result = createStateChart(agent);
		StateChartCombinedActionScheduler.INSTANCE.scheduleBeginTime(begin,
				result);
		return result;
	}

	public static MyStateChart4 createStateChart(MyAgent agent) {
		MyStateChart4Generator generator = new MyStateChart4Generator();
		return generator.build(agent);
	}

	private MyStateChart4(MyAgent agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends
			StateChartBuilder<MyAgent> {

		public MyStateChartBuilder(MyAgent agent,
				AbstractState<MyAgent> entryState, String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
		}

		@Override
		public MyStateChart4 build() {
			MyStateChart4 result = new MyStateChart4(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class MyStateChart4Generator {

		private Map<String, AbstractState<MyAgent>> stateMap = new HashMap<String, AbstractState<MyAgent>>();

		public MyStateChart4 build(MyAgent agent) {
			// Composite State 0
			CompositeState<MyAgent> cs1 = createCS1();
			MyStateChartBuilder mscb = new MyStateChartBuilder(agent, cs1,
					"_YVf2wFuMEeKcW_LgR9Zkzw");

			FinalStateBuilder<MyAgent> ssBuilder2 = new FinalStateBuilder<MyAgent>(
					"Final State 10");
			FinalState<MyAgent> s2 = ssBuilder2.build();
			stateMap.put("_nvI6QVuMEeKcW_LgR9Zkzw", s2);
			mscb.addRootState(s2, "_nvI6QVuMEeKcW_LgR9Zkzw");

			BranchState<MyAgent> branch1 = new BranchStateBuilder<MyAgent>(
					"Choice 11").build();
			stateMap.put("_ohWaUFuMEeKcW_LgR9Zkzw", branch1);
			mscb.addRootState(branch1, "_ohWaUFuMEeKcW_LgR9Zkzw");

			createTransitions(mscb);
			return mscb.build();

		}

		// Creates CompositeState 'Composite State 0'
		private CompositeState<MyAgent> createCS1() {

			SimpleStateBuilder<MyAgent> ssBuilder3 = new SimpleStateBuilder<MyAgent>(
					"State 2");
			SimpleState<MyAgent> s3 = ssBuilder3.build();
			stateMap.put("_d8ab8FuMEeKcW_LgR9Zkzw", s3);

			CompositeStateBuilder<MyAgent> csBuilder1 = new CompositeStateBuilder<MyAgent>(
					"Composite State 0", s3, "_d8ab8FuMEeKcW_LgR9Zkzw");

			HistoryStateBuilder<MyAgent> ssBuilder4 = new HistoryStateBuilder<MyAgent>(
					"Shallow History 7", true);
			HistoryState<MyAgent> s4 = ssBuilder4.build();
			stateMap.put("_lLIscFuMEeKcW_LgR9Zkzw", s4);
			csBuilder1.addHistoryState(s4, "_lLIscFuMEeKcW_LgR9Zkzw");

			HistoryStateBuilder<MyAgent> ssBuilder5 = new HistoryStateBuilder<MyAgent>(
					"Deep History 8", false);
			HistoryState<MyAgent> s5 = ssBuilder5.build();
			stateMap.put("_ltWosVuMEeKcW_LgR9Zkzw", s5);
			csBuilder1.addHistoryState(s5, "_ltWosVuMEeKcW_LgR9Zkzw");

			FinalStateBuilder<MyAgent> ssBuilder6 = new FinalStateBuilder<MyAgent>(
					"Final State 9");
			FinalState<MyAgent> s6 = ssBuilder6.build();
			stateMap.put("_nXXTcVuMEeKcW_LgR9Zkzw", s6);
			csBuilder1.addChildState(s6, "_nXXTcVuMEeKcW_LgR9Zkzw");

			CompositeState<MyAgent> cs1 = csBuilder1.build();
			stateMap.put("_YVf2wFuMEeKcW_LgR9Zkzw", cs1);
			return cs1;
		}

		private void createTransitions(MyStateChartBuilder mscb) {
			// creates transition Transition 12
			createTransition1(mscb);
			// creates transition Transition 13
			createTransition2(mscb);
			// creates transition Transition 22
			createTransition3(mscb);

		}

		// creates transition Transition 12, from = Composite State 0, to = Choice 11
		private void createTransition1(MyStateChartBuilder mscb) {
			TransitionBuilder<MyAgent> tb = new TransitionBuilder<MyAgent>(
					"Transition 12", stateMap.get("_YVf2wFuMEeKcW_LgR9Zkzw"),
					stateMap.get("_ohWaUFuMEeKcW_LgR9Zkzw"));
			tb.addTrigger(new AlwaysTrigger(1.0));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 13, from = Choice 11, to = Final State 10
		private void createTransition2(MyStateChartBuilder mscb) {
			DefaultOutOfBranchTransitionBuilder<MyAgent> tb = new DefaultOutOfBranchTransitionBuilder<MyAgent>(
					"Transition 13", stateMap.get("_ohWaUFuMEeKcW_LgR9Zkzw"),
					stateMap.get("_nvI6QVuMEeKcW_LgR9Zkzw"));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 22, from = State 2, to = Final State 9
		private void createTransition3(MyStateChartBuilder mscb) {
			TransitionBuilder<MyAgent> tb = new TransitionBuilder<MyAgent>(
					"Transition 22", stateMap.get("_d8ab8FuMEeKcW_LgR9Zkzw"),
					stateMap.get("_nXXTcVuMEeKcW_LgR9Zkzw"));
			tb.addTrigger(new ProbabilityTrigger<MyAgent>(
					new SC1TriggerDoubleFunction3(), 1.0));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

	}
}
