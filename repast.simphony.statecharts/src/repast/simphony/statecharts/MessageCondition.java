package repast.simphony.statecharts;

public interface MessageCondition{
	public boolean isMessageConditionTrue(Object message) throws Exception;
}