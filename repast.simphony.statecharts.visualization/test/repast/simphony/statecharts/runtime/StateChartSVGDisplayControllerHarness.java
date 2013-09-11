package repast.simphony.statecharts.runtime;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.environment.Runner;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.statecharts.StateChart;
import repast.simphony.statecharts.StateChartScheduler;
import repast.simphony.statecharts.StateChartSimIntegrator;
import simple.Agent;
import simple.chart.SimpleStatechart;

/**
 * The SimpleStateChart used here was created in a RS user enviroment and
 * dropped into the test/simple.chart package. If this is done in the future,
 * make sure to remove the RepastEssentials static imports in the SCXX type
 * classes. In Eclipse this can be done using the Control-H search file and
 * replace easily.
 * 
 * @author jozik
 * 
 */
public class StateChartSVGDisplayControllerHarness {

	public static void main(String[] args) {
		Schedule schedule = new Schedule();
		Runner runner = new AbstractRunner() {

			@Override
			public void execute(RunState toExecuteOn) {

			}

		};
		RunEnvironment.init(schedule, runner, null, true);
		Context context = new DefaultContext();
		RunState.init().setMasterContext(context);
		Agent agent = new Agent();
		StateChart sc = SimpleStatechart.createStateChart(agent, 0);
		StateChartSVGDisplayController scsdc = new StateChartSVGDisplayController(
				agent, sc);
		scsdc.createAndShowDisplay();
		context.add(agent);
		schedule.execute();
		// StateChartScheduler.beginNow(sc);
		// schedule.execute();
		// sc.begin(new StateChartSimIntegrator() {
		//
		// @Override
		// public void reset() {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public boolean integrate(StateChart<?> chart) {
		// return true;
		// }
		// });
		System.out.println("done.");
	}

}
