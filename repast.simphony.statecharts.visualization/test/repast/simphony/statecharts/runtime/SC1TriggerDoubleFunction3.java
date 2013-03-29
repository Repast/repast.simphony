package repast.simphony.statecharts.runtime;

import repast.simphony.parameter.Parameters;
import repast.simphony.statecharts.Transition;
import repast.simphony.statecharts.TriggerDoubleFunction;

/**
 * Trigger Function for Transition 22, from = State 2, to = Final State 9.
 */
public class SC1TriggerDoubleFunction3 implements
		TriggerDoubleFunction<MyAgent> {
	@Override
	public double value(MyAgent agent, Transition<MyAgent> transition,
			Parameters params) throws Exception {
		return 0.56;

	}
}
