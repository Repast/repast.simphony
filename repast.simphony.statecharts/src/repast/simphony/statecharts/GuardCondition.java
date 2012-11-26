package repast.simphony.statecharts;

public interface GuardCondition<T> {
	public boolean condition(T agent, Transition<T> transition) throws Exception;
}
