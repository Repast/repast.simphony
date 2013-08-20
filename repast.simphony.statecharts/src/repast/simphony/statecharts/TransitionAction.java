package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;

public interface TransitionAction<T> {
	
	public void action(T agent, Transition<T> transition, Parameters params) throws Exception;

}
