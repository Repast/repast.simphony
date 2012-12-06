package repast.simphony.statecharts;

public abstract class AgentTransitionMessageChecker<T> implements MessageChecker {

	protected Transition<T> transition;

	protected void setTransition(Transition<T> transition) {
		this.transition = transition;
	}

	private T agent;

	// For testing purposes only.
	protected void setAgent(T agent) {
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

}
