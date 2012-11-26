package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;

public class AlwaysTrigger extends AbstractTrigger {

	private double initializedTickCount;

	@Override
	public void initialize() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
	}

	@Override
	public boolean isTriggered() {
		return isTriggerConditionTrue();
	}

	@Override
	public boolean isTriggerConditionTrue() {
		return true;
	}

	@Override
	public boolean isRecurring() {
		return true;
	}

	@Override
	public double getInterval() {
		return 1;
	}

	@Override
	public double getNextTime() {
		return initializedTickCount + getInterval();
	}

	@Override
	public boolean canTransitionZeroTime() {
		return true;
	}


}
