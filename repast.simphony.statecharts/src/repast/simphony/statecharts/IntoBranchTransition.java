package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class IntoBranchTransition{
	
	public State getFrom() {
		return from;
	}
	
	public Trigger getFromTrigger() {
		return fromTrigger;
	}
	public double getFromTransitionPriority() {
		return fromTransitionPriority;
	}
	public Callable<Void> getFromOnTransition() {
		return fromOnTransition;
	}
	public Callable<Boolean> getFromGuard() {
		return fromGuard;
	}

	private Trigger fromTrigger;
	private double fromTransitionPriority;
	private Callable<Void> fromOnTransition;
	private Callable<Boolean> fromGuard;
	private State from;

	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger){
		return new IntoBranchTransition(from, fromTrigger, 0, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}
	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger, double fromTransitionPriority){
		return new IntoBranchTransition(from, fromTrigger, fromTransitionPriority, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}
	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger, Callable<Void> fromOnTransition){
		return new IntoBranchTransition(from, fromTrigger, 0, fromOnTransition, Transition.createEmptyGuard());
	}
	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger, Callable<Void> fromOnTransition, Callable<Boolean> fromGuard){
		return new IntoBranchTransition(from, fromTrigger, 0, fromOnTransition, fromGuard);
	}
	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger, Callable<Void> fromOnTransition, double fromTransitionPriority){
		return new IntoBranchTransition(from, fromTrigger, fromTransitionPriority, fromOnTransition, Transition.createEmptyGuard());
	}
	public static IntoBranchTransition createIntoBranchTransition(State from, Trigger fromTrigger, Callable<Void> fromOnTransition, Callable<Boolean> fromGuard, double fromTransitionPriority){
		return new IntoBranchTransition(from, fromTrigger, fromTransitionPriority, fromOnTransition, fromGuard);
	}
	
	
	private IntoBranchTransition(State from,
		Trigger fromTrigger, double fromTransitionPriority, Callable<Void> fromOnTransition,
		Callable<Boolean> fromGuard){
		this.from = from;
		this.fromTrigger = fromTrigger;
		this.fromTransitionPriority = fromTransitionPriority;
		this.fromOnTransition = fromOnTransition;
		this.fromGuard = fromGuard;
	}

}