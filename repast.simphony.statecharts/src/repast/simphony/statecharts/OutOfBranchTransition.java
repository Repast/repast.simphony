package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class OutOfBranchTransition{
	final State toState;
	final Trigger trigger;
	final Callable<Void> onTransition;
	
	public State getToState() {
		return toState;
	}
	public Trigger getTrigger() {
		return trigger;
	}
	public Callable<Void> getOnTransition() {
		return onTransition;
	}
	
	public static OutOfBranchTransition createOutOfBranchTransition(State toState, Callable<Boolean> condition,
			Callable<Void> onTransition){
		return new OutOfBranchTransition(toState, condition, onTransition);
	}
	public static OutOfBranchTransition createOutOfBranchTransition(State toState, Callable<Boolean> condition){
		return new OutOfBranchTransition(toState, condition, Transition.createEmptyOnTransition());
	}
	public static OutOfBranchTransition createDefaultOutOfBranchTransition(State toState, Callable<Void> onTransition){
		return new OutOfBranchTransition(toState,null,onTransition);
	}
	public static OutOfBranchTransition createDefaultOutOfBranchTransition(State toState){
		return new OutOfBranchTransition(toState,null,Transition.createEmptyOnTransition());
	}
	
	private OutOfBranchTransition(State toState, Callable<Boolean> condition,
			Callable<Void> onTransition) {
		this.toState = toState;
		if (condition == null) this.trigger = new AlwaysTrigger();
		else this.trigger = new ConditionTrigger(condition);
		this.onTransition = onTransition;
	}
}