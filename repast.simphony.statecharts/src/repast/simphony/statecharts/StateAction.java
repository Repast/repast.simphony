package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;

public interface StateAction<T> {
	
	public void action(T agent, AbstractState<T> state, Parameters params) throws Exception;

}
