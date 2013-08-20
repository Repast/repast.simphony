package repast.simphony.statecharts;

public class UnconditionalMessageChecker implements MessageChecker {

	@Override
	public boolean checkMessage(Object message) {
		return message != null;
	}

}
