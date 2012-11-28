package repast.simphony.statecharts;


public interface MessageCondition<T>{
	public boolean isTrue(Object message, T agent, Transition<T> transition) throws Exception;
}