package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

public class MessageConditionMessageChecker<T, U> extends
		AgentTransitionMessageChecker<T> {

	private MessageCondition<T, U> messageCondition;
	private Class<? extends U> messageClass;
	private Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}

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
						messageClass.cast(message), getParams());
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
