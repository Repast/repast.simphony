package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

public class ConditionTrigger<T> extends AbstractTrigger<T> {

	private double pollingTime, nextPollingTime;
	private double initializedTickCount;
	private ConditionTriggerCondition<T> condition;
	private Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}


	public ConditionTrigger(ConditionTriggerCondition<T> condition,
			double pollingTime) {
		this.pollingTime = pollingTime;
		nextPollingTime = pollingTime;
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
	
	public void setInterval(double interval){
		nextPollingTime = interval;
	}

	public void initialize() {
		pollingTime = nextPollingTime;
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
			result = condition.condition(getAgent(), transition, getParams());
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
