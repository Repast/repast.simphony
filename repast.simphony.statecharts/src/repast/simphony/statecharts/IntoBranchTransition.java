package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class IntoBranchTransition{
	
	public AbstractState getSource() {
		return source;
	}
	
	public Trigger getTrigger() {
		return trigger;
	}
	public double getTransitionPriority() {
		return transitionPriority;
	}
	public Callable<Void> getOnTransition() {
		return onTransition;
	}
	public Callable<Boolean> getGuard() {
		return guard;
	}

	private Trigger trigger;
	private double transitionPriority;
	private Callable<Void> onTransition;
	private Callable<Boolean> guard;
	private AbstractState source;

	public static IntoBranchTransition createIntoBranchTransition(AbstractState source, Trigger trigger){
		return new IntoBranchTransition(source, trigger, 0, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}

	public static IntoBranchTransition createIntoBranchTransition(AbstractState source, Trigger trigger, double transitionPriority){
		return new IntoBranchTransition(source, trigger, transitionPriority, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}
	
	public void registerGuard(Callable<Boolean> guard) {
		this.guard = guard;
	}
	
	public void registerOnTransition(Callable<Void> onTransition) {
		this.onTransition = onTransition;
	}
	
	
	private IntoBranchTransition(AbstractState source,
		Trigger trigger, double transitionPriority, Callable<Void> onTransition,
		Callable<Boolean> guard){
		this.source = source;
		this.trigger = trigger;
		this.transitionPriority = transitionPriority;
		this.onTransition = onTransition;
		this.guard = guard;
	}

}