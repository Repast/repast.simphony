package repast.simphony.statecharts;


public class SelfTransitionBuilder<T> {
	
	private Trigger trigger;
	private AbstractState<T> state;
	private double priority = 0;
	private TransitionAction<T> onTransition = TransitionBuilder.<T>createEmptyOnTransition();
	private GuardCondition<T> guard = TransitionBuilder.<T>createEmptyGuard();
	
	public SelfTransitionBuilder(AbstractState<T> state){
		this.state = state;
	}
	
	public SelfTransitionBuilder<T> setPriority(double priority){
		this.priority = priority;
		return this;
	}
	
	public SelfTransitionBuilder<T> addTrigger(Trigger trigger){
		this.trigger = trigger;
		return this;
	}
	
//	public void addState(AbstractState<T> state){
//		this.state = state;
//	}
		
	public SelfTransitionBuilder<T> registerOnTransition(TransitionAction<T> onTransition) {
		this.onTransition = onTransition;
		return this;
	}
	
	public SelfTransitionBuilder<T> registerGuard(GuardCondition<T> guard) {
		this.guard = guard;
		return this;
	}
	
	public SelfTransition<T> build(){
		if (trigger == null){
			trigger = new AlwaysTrigger();
		}
//		if (state == null){
//			throw new IllegalStateException("The state must be specified to build a self-transition.");
//		}
		SelfTransition<T> result = new SelfTransition<T>(trigger, state, priority);
		result.registerGuard(guard);
		result.registerOnTransition(onTransition);
		return result;
	}
}
