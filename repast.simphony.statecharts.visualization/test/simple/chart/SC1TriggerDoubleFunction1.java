package simple.chart;

import repast.simphony.statecharts.*;
import repast.simphony.parameter.Parameters;
import static repast.simphony.random.RandomHelper.*;
import repast.simphony.statecharts.generator.GeneratedFor;



import simple.*;

/**
 * Trigger Function for Transition 3, from = State 0, to = Composite State 1.
 */
@GeneratedFor("_2VqLcA_5EeOncZTLucYA7w")
public class SC1TriggerDoubleFunction1 implements TriggerDoubleFunction<Agent> {
	@Override
	public double value(Agent agent, Transition<Agent> transition,
			Parameters params) throws Exception {
		return 2;

	}
}
