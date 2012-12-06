package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.random.RandomHelper;
import simphony.util.messages.MessageCenter;

public class ExponentialDecayRateTrigger<T> extends AbstractTrigger<T> {

	private TriggerDoubleFunction<T> tdf;
	private double currentInterval;
	private double initializedTickCount;


	public ExponentialDecayRateTrigger(final double decayRate) {
		this(new TriggerDoubleFunction<T>(){
			@Override
			public double value(T agent, Transition<T> transition)
					throws Exception {
				return decayRate;
			}

			@Override
			public String toString() {
				return Double.toString(decayRate);
			}			
		});
	}
	
	public ExponentialDecayRateTrigger(TriggerDoubleFunction<T> tdf){
		this.tdf = tdf;
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
		try{
			currentInterval = Math.log(1-rand)/(-tdf.value(getAgent(),transition));
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when evaluating double function: " + tdf + " in " + this, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getNextTime() {
		return initializedTickCount + currentInterval;
	}

	public boolean isTriggered() {
		return isTriggerConditionTrue();
	}

	public boolean isTriggerConditionTrue() {
		return Double.compare(RunEnvironment.getInstance().getCurrentSchedule().getTickCount(),getNextTime()) >= 0;
	}

	public String toString() {
		return "ExponentialDecayRateTrigger with decayRate: " + tdf
				+ " and currentInterval: " + currentInterval;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}
	



}
