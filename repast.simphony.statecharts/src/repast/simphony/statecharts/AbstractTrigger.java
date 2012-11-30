package repast.simphony.statecharts;

public abstract class AbstractTrigger<T> implements Trigger {

	protected Transition<T> transition;
	private T agent;

	protected void setTransition(Transition<T> transition) {
		this.transition = transition;
	}

	@Override
	public boolean isQueueConsuming() {
		return false;
	}

	protected void setAgent(T agent) {
		this.agent = agent;
	}

	protected T getAgent() {
		if (agent == null) {
			if (transition == null) {
				throw new IllegalStateException(
						"The transition was not set in a condition trigger.");
			} else {
				agent = transition.getAgent();
			}
		}
		return agent;
	}

}