package sample4.chart;

import java.util.Map;
import java.util.HashMap;

import repast.simphony.statecharts.*;
import repast.simphony.statecharts.generator.GeneratedFor;

import sample4.*;

@GeneratedFor("_L8QJ0GPqEeK6zsoQEt4knA")
public class Chart2 extends DefaultStateChart<sample4.Agent> {

	public static Chart2 createStateChart(sample4.Agent agent, double begin) {
		Chart2 result = createStateChart(agent);
		StateChartCombinedActionScheduler.INSTANCE.scheduleBeginTime(begin,
				result);
		return result;
	}

	public static Chart2 createStateChart(sample4.Agent agent) {
		Chart2Generator generator = new Chart2Generator();
		return generator.build(agent);
	}

	private Chart2(sample4.Agent agent) {
		super(agent);
	}

	private static class MyStateChartBuilder extends
			StateChartBuilder<sample4.Agent> {

		public MyStateChartBuilder(sample4.Agent agent,
				AbstractState<sample4.Agent> entryState, String entryStateUuid) {
			super(agent, entryState, entryStateUuid);
		}

		@Override
		public Chart2 build() {
			Chart2 result = new Chart2(getAgent());
			setStateChartProperties(result);
			return result;
		}
	}

	private static class Chart2Generator {

		private Map<String, AbstractState<Agent>> stateMap = new HashMap<String, AbstractState<Agent>>();

		public Chart2 build(Agent agent) {
			throw new UnsupportedOperationException(
					"Statechart has not been defined.");

		}

		private void createTransitions(MyStateChartBuilder mscb) {

		}

	}
}
