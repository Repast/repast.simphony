package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateChartBuilder<T> {
	private double priority = 0;
	private TransitionResolutionStrategy trs = TransitionResolutionStrategy.RANDOM;
	
	private List<BranchState<T>> branches = new ArrayList<BranchState<T>>();
	private AbstractState<T> entryState;
	private Set<AbstractState<T>> states = new HashSet<AbstractState<T>>();
	private List<SelfTransition<T>> selfTransitions = new ArrayList<SelfTransition<T>>();
	private List<Transition<T>> regularTransitions = new ArrayList<Transition<T>>();
	public StateChartBuilder(AbstractState<T> entryState){
		registerEntryState(entryState);
	}
	private void registerEntryState(AbstractState<T> entryState){
		this.entryState = entryState;
		states.add(entryState);
	}
	
	public void addRootState(AbstractState<T> state){
		states.add(state);
	}
	
	public void addBranch(BranchState<T> branch){
		branches.add(branch);
	}
	
//	public void addSelfTransition(Trigger trigger, AbstractState<T> state) {
//		addSelfTransition(trigger, Transition.<T>createEmptyOnTransition(),
//				Transition.<T>createEmptyGuard(), state);
//	}
//
//	public void addSelfTransition(Trigger trigger, TransitionAction<T> onTransition,
//			GuardCondition<T> guard, AbstractState<T> state) {
//		Transition<T> transition = new Transition<T>(trigger, state, state);
//		transition.registerOnTransition(onTransition);
//		transition.registerGuard(guard);
//		addSelfTransition(transition);
//	}

	protected void addSelfTransition(SelfTransition<T> transition) {
		selfTransitions.add(transition);
	}
	
	protected void addRegularTransition(Transition<T> transition) {
		addRootState(transition.getSource());
		addRootState(transition.getTarget());
		regularTransitions.add(transition);
	}
	
	public StateChart<T> build(T agent){
		if (entryState == null){
			throw new IllegalStateException(
					"An entry state was not added to the StateChart.");
		}
		DefaultStateChart<T> result = new DefaultStateChart<T>(agent);
		// set priority
		result.setPriority(priority);
		// set trs
		result.setTransitionResolutionStrategy(trs);
		// add root level states
			// add entry state
		result.registerEntryState(entryState);
		for (AbstractState<T> state : states){
			result.addState(state);
		}
			// composite
		for (SelfTransition<T> t : selfTransitions){
			result.addSelfTransition(t);
		}
		for (Transition<T> t : regularTransitions){
			result.addRegularTransition(t);
		}
		for (BranchState<T> b : branches){
			result.addBranch(b);
		}
		return result;
	}
	
}
