package sample1.chart;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;
import repast.simphony.statecharts.generator.GeneratedFor;

import sample1.*;

@GeneratedFor("_alxNQIK9EeK-RcNW8QVYIg")
public class Statechart extends DefaultStateChart<sample1.Agent> {

	public static Statechart createStateChart(sample1.Agent agent, double begin) {
		Statechart result = createStateChart(agent);
		StateChartScheduler.INSTANCE.scheduleBeginTime(begin, result);
		return result;
	}

	public static Statechart createStateChart(sample1.Agent agent) {
		StatechartGenerator generator = new StatechartGenerator();
		return generator.build(agent);
	}

	private Statechart(sample1.Agent agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends
			StateChartBuilder<sample1.Agent> {

		public MyStateChartBuilder(sample1.Agent agent,
				AbstractState<sample1.Agent> entryState, String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
			setPriority(0.0);
		}

		@Override
		public Statechart build() {
			Statechart result = new Statechart(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class StatechartGenerator {

		private Map<String, AbstractState<Agent>> stateMap = new HashMap<String, AbstractState<Agent>>();

		public Statechart build(Agent agent) {
			SimpleStateBuilder<Agent> ssBuilder1 = new SimpleStateBuilder<Agent>(
					"State 0");
			ssBuilder1.registerOnEnter(new SC1OnEnterAction1());
			SimpleState<Agent> s1 = ssBuilder1.build();
			stateMap.put("_ZC5P0IK-EeK-RcNW8QVYIg", s1);
			MyStateChartBuilder mscb = new MyStateChartBuilder(agent, s1,
					"_ZC5P0IK-EeK-RcNW8QVYIg");

			SimpleStateBuilder<Agent> ssBuilder2 = new SimpleStateBuilder<Agent>(
					"State 50");
			ssBuilder2.registerOnEnter(new SC1OnEnterAction2());
			SimpleState<Agent> s2 = ssBuilder2.build();
			stateMap.put("_gQcgEZC-EeKuFP6O5ihiLg", s2);
			mscb.addRootState(s2, "_gQcgEZC-EeKuFP6O5ihiLg");
			SimpleStateBuilder<Agent> ssBuilder3 = new SimpleStateBuilder<Agent>(
					"State 51");
			ssBuilder3.registerOnEnter(new SC1OnEnterAction3());
			SimpleState<Agent> s3 = ssBuilder3.build();
			stateMap.put("_gkgrkZC-EeKuFP6O5ihiLg", s3);
			mscb.addRootState(s3, "_gkgrkZC-EeKuFP6O5ihiLg");
			createTransitions(mscb);
			return mscb.build();

		}

		private void createTransitions(MyStateChartBuilder mscb) {
			// creates transition Transition 52
			createTransition1(mscb);
			// creates transition Transition 53
			createTransition2(mscb);
			// creates transition Transition 54
			createTransition3(mscb);

		}

		// creates transition Transition 52, from = State 0, to = State 50
		private void createTransition1(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 52", stateMap.get("_ZC5P0IK-EeK-RcNW8QVYIg"),
					stateMap.get("_gQcgEZC-EeKuFP6O5ihiLg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction1()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 53, from = State 50, to = State 51
		private void createTransition2(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 53", stateMap.get("_gQcgEZC-EeKuFP6O5ihiLg"),
					stateMap.get("_gkgrkZC-EeKuFP6O5ihiLg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction2()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

		// creates transition Transition 54, from = State 51, to = State 0
		private void createTransition3(MyStateChartBuilder mscb) {
			TransitionBuilder<Agent> tb = new TransitionBuilder<Agent>(
					"Transition 54", stateMap.get("_gkgrkZC-EeKuFP6O5ihiLg"),
					stateMap.get("_ZC5P0IK-EeK-RcNW8QVYIg"));
			tb.addTrigger(new TimedTrigger<Agent>(
					new SC1TriggerDoubleFunction3()));
			tb.setPriority(0.0);
			mscb.addRegularTransition(tb.build());
		}

	}
}
