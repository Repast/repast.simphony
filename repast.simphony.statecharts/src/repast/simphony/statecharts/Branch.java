package repast.simphony.statecharts;

import java.util.List;
import java.util.concurrent.Callable;

public class Branch extends DefaultState {
	private State from;
	private Trigger fromTrigger;
	private double fromTransitionPriority;
	private Callable<Void> fromOnTransition;
	private Callable<Boolean> fromGuard;
	private List<State> tos;
	private List<ConditionTrigger> conditions;
	private TransitionResolutionStrategy originalTRS;
	
	public void initializeBranch(final StateChart st) {
		registerOnEnter(new Callable<Void>(){

			@Override
			public Void call() throws Exception {
				Branch.this.originalTRS = st.getTransitionResolutionStrategy();
				st.setTransitionResolutionStrategy(TransitionResolutionStrategy.PRIORITY);
				return null;
			}
			
		});
		registerOnExit(new Callable<Void>(){

			@Override
			public Void call() throws Exception {
				st.setTransitionResolutionStrategy(Branch.this.originalTRS);
				return null;
			}
			
		});
	}

	public static Branch createBranch(String id, State from,
			Trigger fromTrigger, List<State> tos,
			List<ConditionTrigger> conditions) {
		return createBranch(id, from, fromTrigger, 0,
				Transition.createEmptyOnTransition(),
				Transition.createEmptyGuard(), tos, conditions);
	}
	
	public static Branch createBranch(String id, State from,
			Trigger fromTrigger, double fromTransitionPriority, List<State> tos,
			List<ConditionTrigger> conditions) {
		return createBranch(id, from, fromTrigger, fromTransitionPriority,
				Transition.createEmptyOnTransition(),
				Transition.createEmptyGuard(), tos, conditions);
	}

	public static Branch createBranch(String id, State from,
			Trigger fromTrigger, double fromTransitionPriority, Callable<Void> fromOnTransition,
			Callable<Boolean> fromGuard, List<State> tos,
			List<ConditionTrigger> conditions) {
		Branch branch = new Branch(id);
		branch.from = from;
		branch.fromTrigger = fromTrigger;
		branch.fromTransitionPriority = fromTransitionPriority;
		branch.fromOnTransition = fromOnTransition;
		branch.fromGuard = fromGuard;
		branch.tos = tos;
		branch.conditions = conditions;
		return branch;
	}

	public State getFrom() {
		return from;
	}
	
	public Callable<Void> getFromOnTransition() {
		return fromOnTransition;
	}

	public Callable<Boolean> getFromGuard() {
		return fromGuard;
	}

	public List<State> getTos() {
		return tos;
	}

	public List<ConditionTrigger> getConditions() {
		return conditions;
	}

	private Branch(String id) {
		super(id);
	}

	public Trigger getFromTrigger() {
		return fromTrigger;
	}

	public double getFromTransitionPriority() {
		return fromTransitionPriority;
	}

}
