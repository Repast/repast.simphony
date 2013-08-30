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
			createTransitions(mscb);
			return mscb.build();

		}

		// Creates CompositeState 'Composite State 1'
		private CompositeState<Agent> createCS2() {

			SimpleStateBuilder<Agent> ssBuilder3 = new SimpleStateBuilder<Agent>(
					"State 5");
			SimpleState<Agent> s3 = ssBuilder3.build();
			stateMap.put("_EKUGwQ_9EeOpYuNLQpQPLA", s3);

			CompositeStateBuilder<Agent> csBuilder2 = new CompositeStateBuilder<Agent>(
					"Composite State 1", s3, "_EKUGwQ_9EeOpYuNLQpQPLA");

			SimpleStateBuilder<Agent> ssBuilder4 = new SimpleStateBuilder<Agent>(
					"State 6");
			SimpleState<Agent> s4 = ssBuilder4.build();
			stateMap.put("_EmoPUQ_9EeOpYuNLQpQPLA", s4);
			csBuilder2.addChildState(s4, "_EmoPUQ_9EeOpYuNLQpQPLA");

			CompositeState<Agent> cs2 = csBuilder2.build();
			stateMap.put("_Ag-BgQ_9EeOpYuNLQpQPLA", cs2);
			return cs2;
		}

		private void createTransitions(MyStateChartBuilder mscb) {
			// creates transition Transition 3
			createTransition1(mscb);
			// creates transition Transition 8
			createTransition2(mscb);

		}

		// creates transition Transition 3, from = State 0, to = Composite State 1
		private void createTransition1(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 3", stateMap.get("_AIwV0A_9EeOpYuNLQpQPLA"),
					stateMap.get("_Ag-BgQ_9EeOpYuNLQpQPLA"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction1()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 8, from = State 5, to = State 6
		private void createTransition2(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 8", stateMap.get("_EKUGwQ_9EeOpYuNLQpQPLA"),
					stateMap.get("_EmoPUQ_9EeOpYuNLQpQPLA"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction2()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

	}
}
