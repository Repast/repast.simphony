package repast.simphony.statecharts.runtime;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import simple.Agent;
import simple.chart.SimpleStatechart;

public class StateChartSVGDisplayControllerHarness {

	public static void main(String[] args) {
		Schedule schedule = new Schedule ();
		RunEnvironment.init(schedule, null, null, true);
		Context context = new DefaultContext ();
		RunState.init().setMasterContext(context);
		Agent agent = new Agent();
		StateChartSVGDisplayController scsdc = new StateChartSVGDisplayController(agent, SimpleStatechart.createStateChart(agent));
		scsdc.createAndShowDisplay();
		System.out.println("done.");
	}

}
