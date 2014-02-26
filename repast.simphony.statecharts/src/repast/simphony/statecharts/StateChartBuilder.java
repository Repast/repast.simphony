package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

public class StateChartBuilder<T> {
	private double priority = 0;
	private TransitionResolutionStrategy trs = TransitionResolutionStrategy.PRIORITY;

	private List<BranchState<T>> branches = new ArrayList<BranchState<T>>();
	private AbstractState<T> entryState;
	private Set<AbstractState<T>> states = new LinkedHashSet<AbstractState<T>>();
	private Set<SelfTransition<T>> selfTransitions = new LinkedHashSet<SelfTransition<T>>();
	private Set<Transition<T>> regularTransitions = new LinkedHashSet<Transition<T>>();
	private T agent;
	private Map<AbstractState<T>, String> stateUuidMap = new HashMap<AbstractState<T>, String>();
	private Map<Transition<T>, String> transitionUuidMap = new HashMap<Transition<T>, String>();

	protected T getAgent() {
		return agent;
	}

	public StateChartBuilder(T agent, AbstractState<T> entryState,
			String entryStateUuid) {
		this.agent = agent;
		registerEntryState(entryState, entryStateUuid);
	}

	public StateChartBuilder(T agent, AbstractState<T> entryState) {
		this(agent, entryState, null);
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	protected void registerEntryState(AbstractState<T> entryState, String uuid) {
		this.entryState = entryState;
		addStateToStates(entryState, uuid);
	}

	protected void addStateToStates(AbstractState<T> state, String uuid) {
		if (states.add(state)) {
			if (uuid == null) {
				uuid = UUID.randomUUID().toString();
			}
			stateUuidMap.put(state, uuid);
		}
	}

	protected void registerEntryState(AbstractState<T> entryState) {
		registerEntryState(entryState, null);
	}

	public void addRootState(AbstractState<T> state, String uuid) {
		addStateToStates(state, uuid);
	}

	public void addRootState(AbstractState<T> state) {
		addRootState(state, null);
	}

	public void addSelfTransition(SelfTransition<T> transition) {
		addSelfTransition(transition, null);
	}

	public void addSelfTransition(SelfTransition<T> transition, String uuid) {
		if (selfTransitions.add(transition)) {
			if (uuid == null) {
				uuid = UUID.randomUUID().toString();
			}
			transitionUuidMap.put(transition, uuid);
		}
	}

	public void addRegularTransition(Transition<T> transition) {
		addRegularTransition(transition, null);
	}
	
	public void addRegularTransition(Transition<T> transition, String uuid) {
//		addRootState(transition.getSource());
//		addRootState(transition.getTarget());
		if (regularTransitions.add(transition)){
			if (uuid == null) {
				uuid = UUID.randomUUID().toString();
			}
			transitionUuidMap.put(transition, uuid);
		}
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

		stateChart.setStateUuidMap(stateUuidMap);
		// Get all the state uuid mappings from any composite states
		for (AbstractState<T> state : states) {
			if (state instanceof CompositeState) {
				CompositeState<T> compositeState = (CompositeState<T>) state;
				Map<AbstractState<T>, String> map = compositeState
						.getStateUuidMap();
				for (Entry<AbstractState<T>, String> entry : map.entrySet()) {
					stateChart.putStateUuid(entry.getKey(), entry.getValue());
				}
				compositeState.clearStateUuidMap();
			}
		}
		
		stateChart.setTransitionUuidMap(transitionUuidMap);

	}

	private void findBranches(Collection<AbstractState<T>> states2) {
		for (AbstractState<T> state : states2) {
			if (state instanceof BranchState) {
				BranchState<T> branch = (BranchState<T>) state;
				branches.add(branch);
				continue;
			}
			if (state instanceof CompositeState) {
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
