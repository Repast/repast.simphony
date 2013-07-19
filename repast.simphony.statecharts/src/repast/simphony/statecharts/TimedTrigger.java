package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

public class TimedTrigger<T> extends AbstractTrigger<T> {

	private TriggerDoubleFunction<T> tdf;
	private double nextTime;
	private Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}

	public TimedTrigger(final double time) {
		this(new TriggerDoubleFunction<T>() {
			@Override
			public double value(T agent, Transition<T> transition,
					Parameters params) throws Exception {
				return time;
			}

			@Override
			public String toString() {
				return Double.toString(time);
			}
		});
	}

	public TimedTrigger(TriggerDoubleFunction<T> tdf) {
		this.tdf = tdf;
	}

	@Override
	public boolean isRecurring() {
		return false;
	}

	@Override
	public double getInterval() {
		try {
			return tdf.value(getAgent(), transition, getParams());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}

	@Override
	public void initialize() {
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		try {
			double initializedTickCount = schedule.getTickCount();
			double time = tdf.value(getAgent(), transition, getParams());
			this.nextTime = initializedTickCount + time;
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error(
					"Error encountered when evaluating double function: " + tdf
							+ " in " + this, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public double getNextTime() {
		return nextTime;
	}

	@Override
	public boolean isTriggered() {
		return isTriggerConditionTrue();
	}

	@Override
	public boolean isTriggerConditionTrue() {
		return Double.compare(RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount(), getNextTime()) == 0;
	}

	public String toString() {
		return "TimedTrigger with time: " + tdf;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}
	
	/**
	 * This does nothing as there is no polling time
	 * associated with this trigger type.
	 */
	@Override
	public void setInterval(double interval) {
		// do nothing
	}

}
