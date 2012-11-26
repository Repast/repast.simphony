package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

public class ConditionTrigger<T> extends AbstractTrigger {

	private Transition<T> transition;

	protected void setTransition(Transition<T> transition) {
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
						"The transition was not set in a condition trigger.");
			} else {
				agent = transition.getAgent();
			}
		}
		return agent;
	}

	private final double pollingTime;
	private double initializedTickCount;
	private ConditionTriggerCondition<T> condition;

	public ConditionTrigger(ConditionTriggerCondition<T> condition,
			double pollingTime) {
		this.pollingTime = pollingTime;
		this.condition = condition;
	}

	public ConditionTrigger(ConditionTriggerCondition<T> condition) {
		this(condition, 1);
	}

	@Override
	public boolean isRecurring() {
		return true;
	}

	public double getInterval() {
		return pollingTime;
	}

	public void initialize() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
	}

	@Override
	public double getNextTime() {
		return initializedTickCount + pollingTime;
	}

	public boolean isTriggered() {
		double now = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		return Double.compare(now, getNextTime()) >= 0
				&& isTriggerConditionTrue();
	}

	public boolean isTriggerConditionTrue() {
		boolean result = false;
		try {
			result = condition.condition(getAgent(), transition);
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error(
					"Error encountered when calling condition: " + condition
							+ " in " + this, e);
		}
		return result;
	}

	public String toString() {
		return "ConditionTrigger with pollingTime: " + pollingTime;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return true;
	}

}
