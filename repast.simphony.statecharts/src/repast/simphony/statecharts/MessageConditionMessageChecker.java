package repast.simphony.statecharts;

import simphony.util.messages.MessageCenter;

public class MessageConditionMessageChecker<T, U> extends AgentTransitionMessageChecker<T> {

	private MessageCondition<T, U> messageCondition;
	private Class<? extends U> messageClass;

	public MessageConditionMessageChecker(
			MessageCondition<T, U> messageCondition,
			Class<? extends U> messageClass) {
		this.messageCondition = messageCondition;
		this.messageClass = messageClass;
	}


	@Override
	public boolean checkMessage(Object message) {
		boolean result = false;
		
		if (messageClass.isInstance(message)) {
			try {
				result = messageCondition.isTrue(getAgent(), transition,
						messageClass.cast(message));
			} catch (Exception e) {
				MessageCenter.getMessageCenter(getClass()).error(
						"Error encountered when calling message condition in: "
								+ this, e);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "MessageConditionMessageChecker with " + messageCondition;
	}

}
