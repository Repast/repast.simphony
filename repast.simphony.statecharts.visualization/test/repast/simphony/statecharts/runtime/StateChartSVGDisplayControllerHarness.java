package repast.simphony.statecharts.runtime;

public class StateChartSVGDisplayControllerHarness {

	public static void main(String[] args) {
		MyAgent agent = new MyAgent();
		StateChartSVGDisplayController scsdc = new StateChartSVGDisplayController(agent, MyStateChart4.createStateChart(agent));
		scsdc.createAndShowDisplay();
	}

}
