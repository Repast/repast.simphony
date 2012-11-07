package repast.simphony.statecharts;

public class MessageEqualsMessageChecker<T> implements MessageChecker {

	private T check;
	
	public MessageEqualsMessageChecker(T check){
		this.check = check;
	}
	
	@Override
	public boolean checkMessage(Object message) {
		return check == null ? message == null : check.equals(message);
	}
	

}
