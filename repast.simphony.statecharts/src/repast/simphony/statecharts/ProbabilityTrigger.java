package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import simphony.util.messages.MessageCenter;

public class ProbabilityTrigger<T> extends AbstractTrigger<T> {

	private TriggerDoubleFunction<T> tdf;
	private double pollingTime, nextPollingTime;
	private double initializedTickCount;
	private double probability;
	private Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}

	public ProbabilityTrigger(final double probability, double pollingTime) {
		this(new TriggerDoubleFunction<T>() {
			@Override
			public double value(T agent, Transition<T> transition,
					Parameters params) throws Exception {
				return probability;
			}

			@Override
			public String toString() {
				return Double.toString(probability);
			}
		}, pollingTime);
	}

	public ProbabilityTrigger(double probability) {
		this(probability, 1d);
	}

	public ProbabilityTrigger(TriggerDoubleFunction<T> tdf) {
		this(tdf, 1d);
	}

	public ProbabilityTrigger(TriggerDoubleFunction<T> tdf, double pollingTime) {
		this.tdf = tdf;
		this.pollingTime = pollingTime;
		nextPollingTime = pollingTime;
	}

	@Override
	public boolean isRecurring() {
		return true;
	}

	public double getInterval() {
		return pollingTime;
	}
	
	@Override
	public void setInterval(double interval) {
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
		double rand = RandomHelper.nextDouble();
		try {
			return tdf.value(getAgent(), transition, getParams()) > rand;
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error(
					"Error encountered when evaluating double function: " + tdf
							+ " in " + this, e);
			throw new RuntimeException(e);
		}
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
