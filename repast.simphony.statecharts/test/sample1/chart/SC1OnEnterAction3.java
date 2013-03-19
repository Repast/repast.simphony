package sample1.chart;

import repast.simphony.parameter.Parameters;
import repast.simphony.statecharts.AbstractState;
import repast.simphony.statecharts.StateAction;
import repast.simphony.statecharts.generator.GeneratedFor;
import sample1.Agent;

/**
 * Action for State 51.
 */
@GeneratedFor("_alxNQIK9EeK-RcNW8QVYIg")
public class SC1OnEnterAction3 implements StateAction<Agent> {
	@Override
	public void action(Agent agent, AbstractState<Agent> state,
			Parameters params) throws Exception {
		agent.setState(3);

	}
}
