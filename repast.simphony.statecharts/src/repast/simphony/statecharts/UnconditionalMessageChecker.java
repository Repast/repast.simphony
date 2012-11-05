package repast.simphony.statecharts;

public class UnconditionalMessageChecker implements MessageChecker {

	@Override
	public boolean isValidMessage(Object message) {
		return message != null;
	}

}
