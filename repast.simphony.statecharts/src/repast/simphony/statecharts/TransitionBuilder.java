package repast.simphony.statecharts;


public class TransitionBuilder<T> {
	public static <U> GuardCondition<U> createEmptyGuard() {
		return new EmptyGuard<U>();
	}
	
	public static <U> TransitionAction<U> createEmptyOnTransition() {
		return new EmptyOnTransition<U>();
	}

	private static class EmptyOnTransition<U> implements TransitionAction<U>{
		@Override
		public void action(U agent, Transition<U> transition) throws Exception {
		}
	}
	private static class EmptyGuard<U> implements GuardCondition<U>{

		@Override
		public boolean condition(U agent, Transition<U> transition)
				throws Exception {
			return true;
		}
	}
	
	private Trigger trigger;
	private AbstractState<T> source, target;
	private double priority = 0;
	private TransitionAction<T> onTransition = new EmptyOnTransition<T>();
	private GuardCondition<T> guard = new EmptyGuard<T>();
	
	public TransitionBuilder(AbstractState<T> source, AbstractState<T> target){
		this.source = source;
		this.target = target;
	}
	
	public void setPriority(double priority){
		this.priority = priority;
	}
	
	public void addTrigger(Trigger trigger){
		this.trigger = trigger;
	}
	
	public void addSourceState(AbstractState<T> source){
		this.source = source;
	}
	
	public void addTargetState(AbstractState<T> target){
		this.target = target;
	}
	
	public void registerOnTransition(TransitionAction<T> onTransition) {
		this.onTransition = onTransition;
	}
	
	public void registerGuard(GuardCondition<T> guard) {
		this.guard = guard;
	}
	
	public Transition<T> build(){
		if (trigger == null){
			trigger = new AlwaysTrigger();
		}
		if (source == null){
			throw new IllegalStateException("The source state must be specified to build a transition.");
		}
		if (target == null){
			throw new IllegalStateException("The target state must be specified to build a transition.");
		}
		Transition<T> result = new Transition<T>(trigger, source, target,priority);
		result.registerGuard(guard);
		result.registerOnTransition(onTransition);
		return result;
	}
}
