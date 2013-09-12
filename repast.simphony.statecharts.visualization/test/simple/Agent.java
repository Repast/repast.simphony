package simple;

import repast.simphony.ui.probe.ProbedProperty;
import simple.chart.SimpleStatechart;

public class Agent {

	@ProbedProperty(displayName="SimpleStatechart")
	SimpleStatechart simpleStatechart = SimpleStatechart.createStateChart(this, 0);
	
	public String getSimpleStatechartState(){
		if (simpleStatechart == null) return "";
		Object result = simpleStatechart.getCurrentSimpleState();
		return result == null ? "" : result.toString();
	}

}
