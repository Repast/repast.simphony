package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public class Transition {
	private Trigger trigger;
	private State source, target;
	private double priority;
	private Callable<Void> onTransition = new Callable<Void>(){
		@Override
		public Void call() throws Exception {
			return null;
		}
	};
	private Callable<Boolean> guard = new Callable<Boolean>(){
		@Override
		public Boolean call() throws Exception {
			return true;
		}
	};
	


	public Transition(Trigger trigger, State source, State target) {
		this(trigger,source,target,0);
	}
	
	public Transition(Trigger trigger, State source, State target, double priority){
		this.trigger = trigger;
		this.source = source;
		this.target = target;
		this.priority = priority;
	}
	

	public Trigger getTrigger() {
		return trigger;
	}

	public State getSource() {
		return source;
	}

	public State getTarget() {
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

	public void reschedule(StateChart stateChart, double currentTime) {
		// if recurring && getNextTime is currentTime
		if (trigger.isRecurring()
				&& Double.compare(trigger.getNextTime(), currentTime) == 0) {
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
