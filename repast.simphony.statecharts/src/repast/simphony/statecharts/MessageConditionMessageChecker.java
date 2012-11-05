package repast.simphony.statecharts;

import simphony.util.messages.MessageCenter;

public class MessageConditionMessageChecker implements MessageChecker {
	private MessageCondition messageCondition;
	
	public MessageConditionMessageChecker(MessageCondition messageCondition){
		this.messageCondition = messageCondition;
	}
	
	@Override
	public boolean isValidMessage(Object message) {
		boolean result = false;
		try {
			result = messageCondition.isMessageConditionTrue(message);
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling message condition in: " + this, e);
		}
		return result;
	}
	
	@Override
	public String toString() {
		return "MessageConditionMessageChecker with " + messageCondition;
	}
	

}
