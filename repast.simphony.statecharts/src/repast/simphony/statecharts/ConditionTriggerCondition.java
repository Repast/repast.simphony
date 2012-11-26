package repast.simphony.statecharts;

public interface ConditionTriggerCondition<T> {
	public boolean condition(T agent, Transition<T> transition) throws Exception;
}
