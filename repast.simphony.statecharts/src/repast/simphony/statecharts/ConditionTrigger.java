package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

public class ConditionTrigger<T> extends AbstractTrigger<T> {

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
