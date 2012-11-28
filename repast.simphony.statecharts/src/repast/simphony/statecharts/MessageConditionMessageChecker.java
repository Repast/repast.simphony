package repast.simphony.statecharts;

import simphony.util.messages.MessageCenter;

public class MessageConditionMessageChecker<T> implements MessageChecker {
	private Transition<T> transition;
	
	protected void setTransition(Transition<T> transition){
		this.transition = transition;
	}
	
	private T agent;

	// For testing purposes only.
	protected void setAgent(T agent){
		this.agent = agent;
	}
	
	protected T getAgent() {
		if (agent == null) {
			if (transition == null) {
				throw new IllegalStateException(
						"The transition was not set in a MessageConditionMessageChecker.");
			} else {
				agent = transition.getAgent();
			}
		}
		return agent;
	}
	
	
	private MessageCondition<T> messageCondition;
	
	public MessageConditionMessageChecker(MessageCondition<T> messageCondition){
		this.messageCondition = messageCondition;
	}
	
	@Override
	public boolean checkMessage(Object message) {
		boolean result = false;
		try {
			result = messageCondition.isTrue(message, getAgent(), transition); 
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
