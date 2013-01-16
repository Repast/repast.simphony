package repast.simphony.statecharts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.collections15.ListUtils;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;
import cern.jet.random.Uniform;

public class DefaultStateChart<T> implements StateChart<T> {

	protected DefaultStateChart(T agent) {
		this.agent = agent;
	}

	private TransitionResolutionStrategy transitionResolutionStrategy = TransitionResolutionStrategy.RANDOM;

	protected TransitionResolutionStrategy getTransitionResolutionStrategy() {
		return transitionResolutionStrategy;
	}

	protected void setTransitionResolutionStrategy(
			TransitionResolutionStrategy transitionResolutionStrategy) {
		this.transitionResolutionStrategy = transitionResolutionStrategy;
	}

	private AbstractState<T> entryState;
	private Set<AbstractState<T>> states = new HashSet<AbstractState<T>>();

	// Regular transitions
	protected List<Transition<T>> regularTransitions = new ArrayList<Transition<T>>();
	protected List<Transition<T>> activeRegularTransitions = new ArrayList<Transition<T>>();

	// Self transitions
	protected List<Transition<T>> selfTransitions = new ArrayList<Transition<T>>();
	protected List<Transition<T>> activeSelfTransitions = new ArrayList<Transition<T>>();

	protected SimpleState<T> currentSimpleState;

	private Map<AbstractState<T>, String> stateUuidMap;

	protected void setStateUuidMap(Map<AbstractState<T>, String> stateUuidMap) {
		this.stateUuidMap = stateUuidMap;
	}
	
	protected void putStateUuid(AbstractState<T> state, String uuid){
		stateUuidMap.put(state, uuid);
	}

	protected void registerEntryState(AbstractState<T> state) {
		entryState = state;
		addState(state);
	}

	@Override
	public void begin() {
		if (entryState == null) {
			// illegal state
			throw new IllegalStateException(
					"An entry state was not registered in the StateChart.");
		}
		clearTransitions(null);
		List<AbstractState<T>> statesToEnter = getStatesToEnter(null,
				entryState);
		
		stateInit(statesToEnter);
	}

	private void partitionQueueConsuming(List<Transition<T>> transitions,
			List<Transition<T>> queueConsuming,
			List<Transition<T>> nonQueueConsuming) {
		for (Transition<T> t : transitions) {
			if (t.isTriggerQueueConsuming())
				queueConsuming.add(t);
			else
				nonQueueConsuming.add(t);
		}
	}

	private void stateInit(List<AbstractState<T>> statesToEnter) {
		notifyChangeListeners();
		currentSimpleState = (SimpleState<T>) statesToEnter.get(statesToEnter
				.size() - 1);
		// Entering states from the top down
		for (AbstractState<T> as : statesToEnter) {
			as.onEnter();
		}
		if (currentSimpleState instanceof FinalState) {
			clearTransitions(null);
		} else if (currentSimpleState instanceof BranchState) {
			List<Transition<T>> branchCandidateTransitions = new ArrayList<Transition<T>>();
			for (Transition<T> t : regularTransitions) {
				if (currentSimpleState.getId().equals(t.getSource().getId())) {
					branchCandidateTransitions.add(t);
				}
			}

			Transition<T> defaultTransition = null;
			List<Transition<T>> trueBranchTransitions = new ArrayList<Transition<T>>();
			for (Transition<T> t : branchCandidateTransitions) {
				if (t.getTrigger() instanceof AlwaysTrigger) {
					defaultTransition = t;
				} else {
					if (t.isTransitionConditionTrue()) {
						trueBranchTransitions.add(t);
					}
				}
			}
			if (defaultTransition == null) {
				throw new IllegalStateException(
						"All branch states must define at least one default transition");
			}
			Transition<T> t = chooseOneTransition(trueBranchTransitions);
			if (t == null)
				makeRegularTransition(defaultTransition);
			else
				makeRegularTransition(t);

		} else {
			List<String> statesToEnterStateIds = new ArrayList<String>();
			for (AbstractState<T> as : statesToEnter) {
				statesToEnterStateIds.add(as.getId());
			}
			// look through all defined regular transitions and find those with
			// source.id ==
			// state.id
			List<Transition<T>> newCandidateTransitions = new ArrayList<Transition<T>>();
			for (Transition<T> t : regularTransitions) {
				if (statesToEnterStateIds.contains(t.getSource().getId())) {
					newCandidateTransitions.add(t);
				}
			}

			// Get zero time transitions
			List<Transition<T>> zeroTimeTransitions = new ArrayList<Transition<T>>();
			// for new candidate transitions find all zeroTimeTransitions
			for (Transition<T> tt : newCandidateTransitions) {
				if (tt.canTransitionZeroTime())
					zeroTimeTransitions.add(tt);
			}
			// for existing active regular transitions, only those that are
			// triggered
			for (Transition<T> tt : activeRegularTransitions) {
				if (tt.canTransitionZeroTime() && tt.isTransitionTriggered())
					zeroTimeTransitions.add(tt);
			}

			// Partition zero time transitions into queue consuming and non
			// queue
			// consuming
			List<Transition<T>> queueConsuming = new ArrayList<Transition<T>>();
			List<Transition<T>> nonQueueConsuming = new ArrayList<Transition<T>>();
			partitionQueueConsuming(zeroTimeTransitions, queueConsuming,
					nonQueueConsuming);

			// Find non queue consuming candidate transitions
			List<Transition<T>> nonQueueConsumingCandidates = new ArrayList<Transition<T>>();
			for (Transition<T> tt : nonQueueConsuming) {
				if (tt.isTransitionConditionTrue())
					nonQueueConsumingCandidates.add(tt);
			}

			List<Transition<T>> queueConsumingCandidates = new ArrayList<Transition<T>>();
			// Find queue consuming candidate transitions
			while (true) {
				for (Transition<T> tt : queueConsuming) {
					if (tt.isTransitionConditionTrue())
						queueConsumingCandidates.add(tt);
				}
				if (queueConsumingCandidates.isEmpty()) {
					queue.poll();
					if (queue.isEmpty())
						break;
				} else {
					break;
				}
			}

			// Concatenate queue and non queue consuming candidates and choose
			// one
			Transition<T> t = chooseOneTransition(ListUtils.union(
					nonQueueConsumingCandidates, queueConsumingCandidates));
			// If a zero time transition was found, make that transition
			if (t != null) {
				// if chosen one is queue consuming
				if (t.isTriggerQueueConsuming()) {
					queue.poll();
				}
				makeRegularTransition(t);
			}
			// Otherwise set up the self and regular transitions and initialize
			// them
			else {
				// collect all relevant self transitions and initialize
				for (Transition<T> st : selfTransitions) {
					if (statesToEnterStateIds.contains(st.getSource().getId())) {
						activeSelfTransitions.add(st);
						st.initialize(this);
					}
				}
				// collect all relevant regular transitions and initialize
				activeRegularTransitions.addAll(newCandidateTransitions);
				for (Transition<T> ct : newCandidateTransitions) {
					ct.initialize(this);
				}

			}
		}
	}



	private void clearTransitions(AbstractState<T> as) {
		// deactivate and clear all active transitions
		deactivateTransitions(as, activeSelfTransitions);
		deactivateTransitions(as, activeRegularTransitions);
	}

	private void deactivateTransitions(AbstractState<T> as,
			List<Transition<T>> transitions) {
		List<Transition<T>> candidateTransitions = new ArrayList<Transition<T>>();
		if (as == null) {
			candidateTransitions = transitions;
		} else {
			for (Transition<T> t : transitions) {
				if (t.getSource().getId().equals(as.getId())) {
					candidateTransitions.add(t);
				}
			}
		}
		double now = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		for (Transition<T> t : candidateTransitions) {
			// if trigger's next time is after now,
			Trigger tr = t.getTrigger();
			double nextTime = tr.getNextTime();
			if (Double.compare(nextTime, now) > 0) {
				removeResolveTime(nextTime);
			}
		}
		transitions.removeAll(candidateTransitions);
	}

	protected void addState(AbstractState<T> state) {
		if (!states.contains(state)) {
			state.setStateChart(this);
			states.add(state);
		}
	}

	protected void addRegularTransition(Transition<T> transition) {
		transition.setStateChart(this);
		addState(transition.getSource());
		addState(transition.getTarget());
		regularTransitions.add(transition);
	}

	protected void addSelfTransition(SelfTransition<T> transition) {
		transition.setStateChart(this);
		selfTransitions.add(transition);
	}

	@Override
	public SimpleState<T> getCurrentSimpleState() {
		return currentSimpleState;
	}

	/**
	 * Returns a list of states to exit, in the order that they should be
	 * exited.
	 * 
	 * @param lca
	 * @return
	 */
	private List<AbstractState<T>> getStatesToExit(AbstractState<T> lca) {
		List<AbstractState<T>> statesToExit = new ArrayList<AbstractState<T>>();
		// Gather all states, from current state (simple state) to just before
		// the lca of the source state (not necessarily a simple state)
		AbstractState<T> s = getCurrentSimpleState();
		while (s != lca && s != null) {
			if (s instanceof CompositeState<?>) {
				CompositeState<T> cs = (CompositeState<T>) s;
				for (HistoryState<T> hs : cs.getHistoryStates()) {
					if (hs.isShallow()) {
						hs.setDestination(statesToExit.get(statesToExit.size() - 1));
					} else {
						hs.setDestination(getCurrentSimpleState());
					}
				}
			}
			statesToExit.add(s);
			s = s.getParent();
		}
		return statesToExit;
	}

	/**
	 * Returns a list of states to enter, in the order that they should be
	 * entered.
	 * 
	 * @param lca
	 * @param target
	 * @return
	 */
	private List<AbstractState<T>> getStatesToEnter(AbstractState<T> lca,
			AbstractState<T> target) {
		LinkedList<AbstractState<T>> statesToEnter = new LinkedList<AbstractState<T>>();
		Map<CompositeState<T>, HistoryState<T>> compositeHistoryMap = new HashMap<CompositeState<T>, HistoryState<T>>();
		AbstractState<T> t = target;
		while (!(t instanceof SimpleState)) {
			if (t instanceof CompositeState) {
				CompositeState<T> cs = (CompositeState<T>) t;
				t = cs.getEntryState();
			} else {
				if (t instanceof HistoryState) {
					HistoryState<T> hs = (HistoryState<T>) t;
					compositeHistoryMap.put(hs.getParent(), hs);
					t = hs.getDestination();
				}
			}
		}
		// At this point t should be the target simple state
		while (t != lca && t != null) {
			if (compositeHistoryMap.containsKey(t)) {
				statesToEnter.addFirst(compositeHistoryMap.get(t));
			}
			statesToEnter.addFirst(t);
			t = t.getParent();
		}
		return statesToEnter;
	}

	private void makeRegularTransition(Transition<T> transition) {
		AbstractState<T> source = transition.getSource();
		AbstractState<T> target = transition.getTarget();

		AbstractState<T> lca = source.calculateLowestCommonAncestor(target);
		List<AbstractState<T>> statesToExit = getStatesToExit(lca);

		currentSimpleState = null;
		for (AbstractState<T> as : statesToExit) {
			clearTransitions(as);
			as.onExit();
		}

		// Transition action
		transition.onTransition();

		List<AbstractState<T>> statesToEnter = getStatesToEnter(lca, target);
		
		stateInit(statesToEnter);
	}

	// private List<Transition>

	private List<Transition<T>> getTriggeredTransitions(
			List<Transition<T>> transitions) {
		List<Transition<T>> triggeredTransitions = new ArrayList<Transition<T>>();
		for (Transition<T> t : transitions) {
			if (t.isTransitionTriggered())
				triggeredTransitions.add(t);
		}
		return triggeredTransitions;
	}

	protected void resolve() {
		// Partition active self transitions into queue consuming and non queue
		// consuming
		List<Transition<T>> queueConsumingActiveSelfTransitions = new ArrayList<Transition<T>>();
		List<Transition<T>> nonQueueConsumingActiveSelfTransitions = new ArrayList<Transition<T>>();
		partitionQueueConsuming(activeSelfTransitions,
				queueConsumingActiveSelfTransitions,
				nonQueueConsumingActiveSelfTransitions);

		// Execute all non queue consuming active self transitions
		for (Transition<T> t : getTriggeredTransitions(nonQueueConsumingActiveSelfTransitions)) {
			t.onTransition();
		}

		// Partition active regular transitions into queue consuming and non
		// queue consuming
		List<Transition<T>> queueConsumingActiveRegularTransitions = new ArrayList<Transition<T>>();
		List<Transition<T>> nonQueueConsumingActiveRegularTransitions = new ArrayList<Transition<T>>();
		partitionQueueConsuming(activeRegularTransitions,
				queueConsumingActiveRegularTransitions,
				nonQueueConsumingActiveRegularTransitions);

		// Find non queue consuming candidate regular transitions
		List<Transition<T>> nonQueueConsumingRegularCandidates = getTriggeredTransitions(nonQueueConsumingActiveRegularTransitions);
		List<Transition<T>> queueConsumingRegularCandidates;
		while (true) {
			// Execute all queue consuming active self transitions
			for (Transition<T> t : getTriggeredTransitions(queueConsumingActiveSelfTransitions)) {
				t.onTransition();
			}

			// Look for queue consuming regular candidates for current queue
			// state
			queueConsumingRegularCandidates = getTriggeredTransitions(queueConsumingActiveRegularTransitions);
			if (queueConsumingRegularCandidates.isEmpty()) {
				queue.poll();
				if (queue.isEmpty())
					break;
			} else {
				break;
			}
		}

		// Concatenate queue and non queue consuming candidates and choose one
		Transition<T> t = chooseOneTransition(ListUtils.union(
				nonQueueConsumingRegularCandidates,
				queueConsumingRegularCandidates));

		// reschedule selfTransitions
		rescheduleTransitions(activeSelfTransitions, false);

		// If a zero time transition was found, make that transition
		if (t != null) {
			// if chosen one is queue consuming
			if (t.isTriggerQueueConsuming()) {
				queue.poll();
			}
			makeRegularTransition(t);
		} else {
			// reschedule regularTransitions
			rescheduleTransitions(activeRegularTransitions, true);
		}

	}

	private Transition<T> chooseOneTransition(List<Transition<T>> transitions) {
		// If no transitions, return null
		if (transitions.isEmpty())
			return null;

		// If there is one valid transition, make the transition
		if (transitions.size() == 1)
			return transitions.get(0);
		// Otherwise resolve based on the StateChart's
		// TransitionResolutionStrategy
		switch (transitionResolutionStrategy) {
		case NATURAL:
			return transitions.get(0);
		case PRIORITY:
			List<Transition<T>> temp = new ArrayList<Transition<T>>(transitions);
			SimUtilities.shuffle(temp, RandomHelper.getUniform());
			Collections.sort(temp, pComp);
			return temp.get(0);
		case RANDOM:
			int size = transitions.size();
			Uniform defaultUniform = RandomHelper.getUniform();
			int index = defaultUniform.nextIntFromTo(0, size - 1);
			return transitions.get(index);
		default:
			throw new IllegalArgumentException();
		}
	}

	private void rescheduleTransitions(List<Transition<T>> activeTransitions,
			boolean regular) {
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		// for each active transition
		if (regular) {
			for (Transition<T> t : activeTransitions) {
				t.rescheduleRegularTransition(this, currentTime);

			}
		} else {
			for (Transition<T> t : activeTransitions) {
				t.rescheduleSelfTransition(this, currentTime);
			}
		}
	}

	private Comparator<Transition<T>> pComp = new PriorityComparator<T>();

	/**
	 * Compares Transitions according to their priority. Lower priority later in
	 * order.
	 */
	static class PriorityComparator<U> implements Comparator<Transition<U>> {
		public int compare(Transition<U> t1, Transition<U> t2) {
			double index1 = t1.getPriority();
			double index2 = t2.getPriority();
			return index1 < index2 ? 1 : index1 == index2 ? 0 : -1;
		}
	}

	protected void scheduleResolveTime(double nextTime) {
		StateChartCombinedActionScheduler.INSTANCE.scheduleResolveTime(
				nextTime, this);
	}

	protected void removeResolveTime(double nextTime) {
		StateChartCombinedActionScheduler.INSTANCE.removeResolveTime(nextTime,
				this);
	}

	private Queue<Object> queue = new ArrayDeque<Object>();

	protected Queue<Object> getQueue() {
		return queue;
	}

	@Override
	public void receiveMessage(Object message) {
		queue.add(message);
	}

	private double priority = 0;

	protected double getPriority() {
		return priority;
	}

	protected void setPriority(double priority) {
		this.priority = priority;
	}

	private T agent;

	protected T getAgent() {
		if (agent == null) {
			throw new IllegalStateException("The agent was not set in: " + this);
		}
		return agent;
	}

	Parameters params;

	protected Parameters getParams() {
		if (params == null) {
			RunEnvironment re = RunEnvironment.getInstance();
			if (re != null)
				params = re.getParameters();
		}
		return params;
	}

	@Override
	public boolean withinState(String id) {
		List<AbstractState<T>> currentStates = getCurrentStates();
		for (AbstractState<T> s : currentStates) {
			if (s.getId().equals(id))
				return true;
		}
		return false;
	}

	@Override
	public List<AbstractState<T>> getCurrentStates() {
		List<AbstractState<T>> states = new ArrayList<AbstractState<T>>();
		AbstractState<T> s = getCurrentSimpleState();
		while (s != null) {
			states.add(s);
			s = s.getParent();
		}
		return states;
	}

	@Override
	public String getUuidForState(AbstractState<T> state) {
		String result = stateUuidMap.get(state);
		return result;
	}

	private Set<StateChartListener> stateChartListeners = new LinkedHashSet<StateChartListener>();
	
	@Override
	public void registerStateChartListener(StateChartListener scl) {
		stateChartListeners.add(scl);
	}

	@Override
	public void removeStateChartListener(StateChartListener scl) {
		stateChartListeners.remove(scl);
	}
	
	private void notifyChangeListeners() {
		for (StateChartListener scl : stateChartListeners){
			scl.update();
		}
		
	}

}
