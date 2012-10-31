package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class Transition implements TriggerListener {
	private Trigger trigger;
	private State source, target;
	private double priority;
	


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
		return trigger.isTriggered();
	}
	
	public boolean isRecurring(){
		return trigger.isRecurring();
	}
	
	public boolean canTransitionZeroTime(){
		return trigger.canTransitionZeroTime();
	}

	public double getPriority() {
		return priority;
	}


	public void initialize(StateChart sc) {
		trigger.initialize(this);
		sc.scheduleResolveTime(trigger.getNextTime());
	}
	
	public void deactivate(StateChart sc, double time) {
		sc.removeResolveTime(time);
	}

	TransitionListener transitionListener;

	@Override
	public void update() {
		// In the future, the guard checks may go here.
		transitionListener.updateRegularTransition(this);
	}

	public void onTransition() {
		System.out.println("Making transition from: " + source.getId()
				+ " to: " + target.getId() + " via: " + trigger);
	}
	

}
