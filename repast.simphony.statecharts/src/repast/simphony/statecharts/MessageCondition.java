package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;


public interface MessageCondition<T,U>{
	public boolean isTrue(T agent, Transition<T> transition, U message, Parameters params) throws Exception;
}