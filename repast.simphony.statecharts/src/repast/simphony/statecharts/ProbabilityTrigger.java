package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.random.RandomHelper;

public class ProbabilityTrigger extends AbstractTrigger {

	private final double pollingTime;
	private double initializedTickCount;
	private double probability;

	public ProbabilityTrigger(double probability, double pollingTime) {
		this.pollingTime = pollingTime;
		this.probability = probability;
	}

	public ProbabilityTrigger(double probability) {
		this(probability, 1);
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
		return Double.compare(now, getNextTime()) >= 0 && isTriggerConditionTrue();
	}

	public boolean isTriggerConditionTrue() {
		double rand = RandomHelper.nextDouble();
		return probability > rand;
	}

	public String toString() {
		return "ProbabilityTrigger with probability: " + probability
				+ " and pollingTime: " + pollingTime;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}
	


}
