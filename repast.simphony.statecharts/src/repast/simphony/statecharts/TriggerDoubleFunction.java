package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;

public interface TriggerDoubleFunction<T> {
	public double value(T agent, Transition<T> transition, Parameters params) throws Exception;
}
