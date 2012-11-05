package repast.simphony.statecharts;

public class UnconditionalByClassMessageChecker implements MessageChecker {

	private Class<?> messageClass;
	
	public UnconditionalByClassMessageChecker(Class<?> messageClass){
		this.messageClass = messageClass;
	}
	
	@Override
	public boolean isValidMessage(Object message) {
		return messageClass.isInstance(message);
	}


}
