package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.random.RandomHelper;

public class ExponentialDecayRateTrigger extends AbstractTrigger {

	private double currentInterval;
	private double initializedTickCount;
	private double decayRate;

	public ExponentialDecayRateTrigger(double decayRate) {
		this.decayRate = decayRate;
	}

	@Override
	public boolean isRecurring() {
		return false;
	}

	public double getInterval() {
		return currentInterval;
	}

	public void initialize() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
		double rand = RandomHelper.nextDouble();
		currentInterval = Math.log(1-rand)/(-decayRate);
	}

	@Override
	public double getNextTime() {
		return initializedTickCount + currentInterval;
	}

	public boolean isTriggered() {
		return isValid();
	}

	public boolean isValid() {
		return Double.compare(RunEnvironment.getInstance().getCurrentSchedule().getTickCount(),getNextTime()) >= 0;
	}

	public String toString() {
		return "ExponentialDecayRateTrigger with decayRate: " + decayRate
				+ " and currentInterval: " + currentInterval;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}
	
	@Override
	public boolean isQueueConsuming() {
		return false;
	}


}
