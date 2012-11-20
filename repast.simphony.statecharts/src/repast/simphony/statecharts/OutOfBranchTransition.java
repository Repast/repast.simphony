package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class OutOfBranchTransition<T>{
	final AbstractState<T> target;
	final Trigger trigger;
	Callable<Void> onTransition;
	
	public AbstractState<T> getTarget() {
		return target;
	}
	public Trigger getTrigger() {
		return trigger;
	}
	public Callable<Void> getOnTransition() {
		return onTransition;
	}
	
	public static <U> OutOfBranchTransition<U> createOutOfBranchTransition(AbstractState<U> toState, Callable<Boolean> condition){
		return new OutOfBranchTransition<U>(toState, condition, Transition.createEmptyOnTransition());
	}

	public static <U> OutOfBranchTransition<U> createDefaultOutOfBranchTransition(AbstractState<U> toState){
		return new OutOfBranchTransition<U>(toState,null,Transition.createEmptyOnTransition());
	}
	
	private OutOfBranchTransition(AbstractState<T> toState, Callable<Boolean> condition,
			Callable<Void> onTransition) {
		this.target = toState;
		if (condition == null) this.trigger = new AlwaysTrigger();
		else this.trigger = new ConditionTrigger(condition);
		this.onTransition = onTransition;
	}
	
	public void registerOnTransition(Callable<Void> onTransition) {
		this.onTransition = onTransition;
	}
}