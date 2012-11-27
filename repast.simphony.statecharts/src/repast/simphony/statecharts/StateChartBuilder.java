package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.Collection;
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
	private T agent;

	protected T getAgent() {
		return agent;
	}

	public StateChartBuilder(T agent, AbstractState<T> entryState) {
		this.agent = agent;
		registerEntryState(entryState);
	}

	public void registerEntryState(AbstractState<T> entryState) {
		this.entryState = entryState;
		states.add(entryState);
	}

	public void addRootState(AbstractState<T> state) {
		states.add(state);
	}

	public void addSelfTransition(SelfTransition<T> transition) {
		selfTransitions.add(transition);
	}

	public void addRegularTransition(Transition<T> transition) {
		addRootState(transition.getSource());
		addRootState(transition.getTarget());
		regularTransitions.add(transition);
	}

	protected void setStateChartProperties(DefaultStateChart<T> stateChart) {
		
		if (entryState == null) {
			throw new IllegalStateException(
					"An entry state was not added to the StateChart.");
		}
		
		// set priority
		stateChart.setPriority(priority);
		// set trs
		stateChart.setTransitionResolutionStrategy(trs);

		// add entry state
		stateChart.registerEntryState(entryState);

		// add root level states
		for (AbstractState<T> state : states) {
			stateChart.addState(state);
		}
		
		// add self transitions
		for (SelfTransition<T> t : selfTransitions) {
			stateChart.addSelfTransition(t);
		}

		// add regular transitions
		for (Transition<T> t : regularTransitions) {
			stateChart.addRegularTransition(t);
		}
		
		// find and initialize all branch states
		findBranches(states);
		for (BranchState<T> b : branches) {
			b.initializeBranch(stateChart);
		}
		
	}

	private void findBranches(Collection<AbstractState<T>> states2) {
		for (AbstractState<T> state : states2){
			if (state instanceof BranchState){
				BranchState<T> branch = (BranchState<T>) state;
				branches.add(branch);
				continue;
			}
			if (state instanceof CompositeState){
				CompositeState<T> compositeState = (CompositeState<T>) state;
				findBranches(compositeState.children);
			}
		}
	}

	public StateChart<T> build() {
		DefaultStateChart<T> result = new DefaultStateChart<T>(getAgent());
		setStateChartProperties(result);
		return result;
	}

}
