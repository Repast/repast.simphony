package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import simphony.util.messages.MessageCenter;

public class Transition {
	private Trigger trigger;
	private State source, target;
	private double priority;
	private Callable<Void> onTransition;
	private Callable<Boolean> guard;
	


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
	
	public boolean isValid(){
		return trigger.isValid();
	}
	
	public boolean isTriggered(){
		return trigger.isTriggered() && checkGuard();
	}
	
	private boolean checkGuard() {
		if (guard == null) return true;
		boolean result = false;
		try {
			result = guard.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when checking guard: " + guard + " in " + this, e);
		}
		return result;
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
	
	public void deactivate(StateChart sc, double time) {
		sc.removeResolveTime(time);
	}

	
	public void registerOnTransition(Callable<Void> onTransition) {
		this.onTransition = onTransition;
	}
	
	
	
	public void onTransition() {
		if(onTransition == null) return;
		try {
			onTransition.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling onTransition in transition: " + this, e);
		}
	}
	
	@Override
	public String toString(){
		return "Transition(" + trigger + ", " + source + ", " + target + ", " + priority + ")"; 
	}
	
//	TransitionListener transitionListener;
//
//	@Override
//	public void update() {
//		// In the future, the guard checks may go here.
//		transitionListener.updateRegularTransition(this);
//	}

	
	

}
