package repast.simphony.statecharts;

public interface MessageCondition{
	public boolean isTrue(Object message) throws Exception;
}