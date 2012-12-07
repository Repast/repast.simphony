package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;


public interface MessageEquals<T>{
	public Object messageValue(T agent, Transition<T> transition, Parameters params) throws Exception;
}