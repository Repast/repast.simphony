package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public class Transition {
	
	private static class EmptyGuard implements Callable<Boolean>{
		@Override
		public Boolean call() throws Exception {
			return true;
		}
	}
	
	private static class EmptyOnTransition implements Callable<Void>{
		@Override
		public Void call() throws Exception {
			return null;
		}
	}
	
	public static Callable<Boolean> createEmptyGuard() {
		return new EmptyGuard();
	}
	
	public static Callable<Void> createEmptyOnTransition() {
		return new EmptyOnTransition();
	}
	
	private Trigger trigger;
	private AbstractState source, target;
	private double priority;
	private Callable<Void> onTransition = new EmptyOnTransition();
	private Callable<Boolean> guard = new EmptyGuard();
	


	public Transition(Trigger trigger, AbstractState source, AbstractState target) {
		this(trigger,source,target,0);
	}
	
	public Transition(Trigger trigger, AbstractState source, AbstractState target, double priority){
		this.trigger = trigger;
		this.source = source;
		this.target = target;
		this.priority = priority;
	}
	

	public Trigger getTrigger() {
		return trigger;
	}

	public AbstractState getSource() {
		return source;
	}

	public AbstractState getTarget() {
		return target;
	}
	
	public boolean isTransitionConditionTrue(){
		return trigger.isTriggerConditionTrue() && checkGuard();
	}
	
	public boolean isTransitionTriggered(){
		return trigger.isTriggered() && checkGuard();
	}
	
	private boolean checkGuard() {
		try {
			return guard.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when checking guard: " + guard + " in " + this, e);
			throw new RuntimeException(e);
		}
	}

	public void registerGuard(Callable<Boolean> guard) {
		this.guard = guard;
	}
	
	public boolean canTransitionZeroTime(){
		return trigger.canTransitionZeroTime();
	}
	
	public boolean isTriggerQueueConsuming(){
		return trigger.isQueueConsuming();
	}
	
	public double getPriority() {
		return priority;
	}


	public void initialize(StateChart sc) {
		trigger.initialize();
		sc.scheduleResolveTime(trigger.getNextTime());
	}

	
	public void registerOnTransition(Callable<Void> onTransition) {
		this.onTransition = onTransition;
	}
	
	public void onTransition() {
		try {
			onTransition.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onTransition in transition: " + this, e);
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public String toString(){
		return "Transition(" + trigger + ", " + source + ", " + target + ", " + priority + ")"; 
	}

	public void rescheduleRegularTransition(StateChart stateChart, double currentTime) {
		// if recurring && getNextTime is currentTime
		if (trigger.isRecurring()
				&& Double.compare(trigger.getNextTime(), currentTime) == 0) {
			// reset to next time and reschedule
			initialize(stateChart);
		}
	}
	
	public void rescheduleSelfTransition(StateChart stateChart, double currentTime) {
		// if getNextTime is currentTime
		if (Double.compare(trigger.getNextTime(), currentTime) == 0) {
			// reset to next time and reschedule
			initialize(stateChart);
		}
	}
	
//	TransitionListener transitionListener;
//
//	@Override
//	public void update() {
//		// In the future, the guard checks may go here.
//		transitionListener.updateRegularTransition(this);
//	}

	
	

}
