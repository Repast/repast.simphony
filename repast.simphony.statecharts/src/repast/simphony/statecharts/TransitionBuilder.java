package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;


public class TransitionBuilder<T> {
	public static <U> GuardCondition<U> createEmptyGuard() {
		return new EmptyGuard<U>();
	}
	
	public static <U> TransitionAction<U> createEmptyOnTransition() {
		return new EmptyOnTransition<U>();
	}

	private static class EmptyOnTransition<U> implements TransitionAction<U>{
		@Override
		public void action(U agent, Transition<U> transition, Parameters params) throws Exception {
		}
	}
	private static class EmptyGuard<U> implements GuardCondition<U>{

		@Override
		public boolean condition(U agent, Transition<U> transition, Parameters params)
				throws Exception {
			return true;
		}
	}
	
	private Trigger trigger;
	private AbstractState<T> source, target;
	private double priority = 0;
	private TransitionAction<T> onTransition = new EmptyOnTransition<T>();
	private GuardCondition<T> guard = new EmptyGuard<T>();
	private String id;
	
	public TransitionBuilder(String id, AbstractState<T> source, AbstractState<T> target){
		this.id = id;
		this.source = source;
		this.target = target;
	}
	
	public TransitionBuilder(AbstractState<T> source, AbstractState<T> target){
		this("",source,target);
	}
	
	public void setPriority(double priority){
		this.priority = priority;
	}
	
	public void addTrigger(Trigger trigger){
		this.trigger = trigger;
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
		Transition<T> result = new Transition<T>(id, trigger, source, target,priority);
		result.registerGuard(guard);
		result.registerOnTransition(onTransition);
		return result;
	}
}
