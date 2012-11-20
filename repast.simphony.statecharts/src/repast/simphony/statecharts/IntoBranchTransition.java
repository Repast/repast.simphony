package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class IntoBranchTransition<T>{
	
	public AbstractState<T> getSource() {
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
	private AbstractState<T> source;

	public static <U> IntoBranchTransition<U> createIntoBranchTransition(AbstractState<U> source, Trigger trigger){
		return new IntoBranchTransition<U>(source, trigger, 0, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}

	public static <U> IntoBranchTransition<U> createIntoBranchTransition(AbstractState<U> source, Trigger trigger, double transitionPriority){
		return new IntoBranchTransition<U>(source, trigger, transitionPriority, Transition.createEmptyOnTransition(), Transition.createEmptyGuard());
	}
	
	public void registerGuard(Callable<Boolean> guard) {
		this.guard = guard;
	}
	
	public void registerOnTransition(Callable<Void> onTransition) {
		this.onTransition = onTransition;
	}
	
	
	private IntoBranchTransition(AbstractState<T> source,
		Trigger trigger, double transitionPriority, Callable<Void> onTransition,
		Callable<Boolean> guard){
		this.source = source;
		this.trigger = trigger;
		this.transitionPriority = transitionPriority;
		this.onTransition = onTransition;
		this.guard = guard;
	}

}