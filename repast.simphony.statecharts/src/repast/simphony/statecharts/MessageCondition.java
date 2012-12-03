package repast.simphony.statecharts;


public interface MessageCondition<T,U>{
	public boolean isTrue(T agent, Transition<T> transition, U message) throws Exception;
}