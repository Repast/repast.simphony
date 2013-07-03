package repast.simphony.statecharts;

import java.util.Queue;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.parameter.Parameters;
import simphony.util.messages.MessageCenter;

public class Transition<T> {

	private DefaultStateChart<T> stateChart;
	private T agent;
	private Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}

	protected T getAgent() {
		if (agent == null) {
			if (stateChart == null) {
				throw new IllegalStateException("The stateChart was not set in: " + this);
			} else {
				agent = stateChart.getAgent();
			}
		}
		return agent;
	}

	private Queue<Object> queue;

	protected Queue<Object> getQueue() {
		if (queue == null) {
			if (stateChart == null) {
				throw new IllegalStateException("The stateChart was not set in: " + this);
			} else {
				queue = stateChart.getQueue();
			}
		}
		return queue;
	}

	protected void setStateChart(DefaultStateChart<T> stateChart) {
		this.stateChart = stateChart;
	}

	private Trigger trigger;
	private AbstractState<T> source, target;
	private double priority;
	private TransitionAction<T> onTransition;
	private GuardCondition<T> guard;
	private String id;

	protected Transition(Trigger trigger, AbstractState<T> source, AbstractState<T> target) {
		this(trigger, source, target, 0);
	}

	protected Transition(Trigger trigger, AbstractState<T> source, AbstractState<T> target,
			double priority) {
		this("", trigger, source, target, priority);
	}

	protected Transition(String id, Trigger trigger, AbstractState<T> source,
			AbstractState<T> target, double priority) {
		this.id = id;
		this.trigger = trigger;
		this.source = source;
		this.target = target;
		this.priority = priority;
		if (trigger instanceof AbstractTrigger) {
			@SuppressWarnings("unchecked")
			AbstractTrigger<T> at = (AbstractTrigger<T>) trigger;
			at.setTransition(this);
		}
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public AbstractState<T> getSource() {
		return source;
	}

	public AbstractState<T> getTarget() {
		return target;
	}

	protected boolean isTransitionConditionTrue() {
		return trigger.isTriggerConditionTrue() && checkGuard();
	}

	protected boolean isTransitionTriggered() {
		return trigger.isTriggered() && checkGuard();
	}

	protected boolean isResolveNow() {
		RunEnvironment re = RunEnvironment.getInstance();
		if (re != null) {
			ISchedule schedule = re.getCurrentSchedule();
			if (schedule != null) {
				double now = schedule.getTickCount();
				return Double.compare(now, trigger.getNextTime()) >= 0;
			}
		}
		return false;
	}

	private boolean checkGuard() {
		try {
			return guard.condition(getAgent(), this, getParams());
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error(
					"Error encountered when checking guard: " + guard + " in " + this, e);
			throw new RuntimeException(e);
		}
	}

	protected void registerGuard(GuardCondition<T> guard) {
		this.guard = guard;
	}

	protected boolean canTransitionZeroTime() {
		return trigger.canTransitionZeroTime();
	}

	protected boolean isTriggerQueueConsuming() {
		return trigger.isQueueConsuming();
	}

	public double getPriority() {
		return priority;
	}

	protected void initialize(DefaultStateChart<T> sc) {
		trigger.initialize();
		sc.scheduleResolveTime(trigger.getNextTime());
	}

	protected void registerOnTransition(TransitionAction<T> onTransition) {
		this.onTransition = onTransition;
	}

	protected void onTransition() {
		try {
			onTransition.action(getAgent(), this, getParams());
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error(
					"Error encountered when calling onTransition in transition: " + this, e);
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		return "Transition(\"" + id + "\", " + trigger + ", " + source + ", " + target + ", "
				+ priority + ")";
	}

	protected void rescheduleRegularTransition(DefaultStateChart<T> stateChart, double currentTime) {
		// if recurring && getNextTime is currentTime
		if (trigger.isRecurring() && Double.compare(trigger.getNextTime(), currentTime) == 0) {
			// reset to next time and reschedule
			initialize(stateChart);
		}
	}

	protected void rescheduleSelfTransition(DefaultStateChart<T> stateChart, double currentTime) {
		// if getNextTime is currentTime
		if (Double.compare(trigger.getNextTime(), currentTime) == 0) {
			// reset to next time and reschedule
			initialize(stateChart);
		}
	}

}
