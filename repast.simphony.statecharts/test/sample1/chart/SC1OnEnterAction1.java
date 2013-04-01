package sample1.chart;

import repast.simphony.statecharts.*;
import repast.simphony.parameter.Parameters;
import repast.simphony.statecharts.generator.GeneratedFor;


import sample1.*;

/**
 * Action for State 0.
 */
@GeneratedFor("_alxNQIK9EeK-RcNW8QVYIg")
public class SC1OnEnterAction1 implements StateAction<Agent> {
	@Override
	public void action(Agent agent, AbstractState<Agent> state,
			Parameters params) throws Exception {
		agent.setState(1);

	}
}
