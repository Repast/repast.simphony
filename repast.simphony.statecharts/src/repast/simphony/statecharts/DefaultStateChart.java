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
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.collections15.ListUtils;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;
import cern.jet.random.Uniform;

public class DefaultStateChart<T> implements StateChart<T> {

	/**
	 * Creates DefaultStateChart for the specified agent and sets whether or not
	 * the agent needs to be in a Context for the statechart to function.
	 * 
	 * @param agent
	 * @param contextRequired
	 */
	protected DefaultStateChart(T agent) {
		this.agent = agent;
	}

	private TransitionResolutionStrategy transitionResolutionStrategy = TransitionResolutionStrategy.PRIORITY;

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

	protected void putStateUuid(AbstractState<T> state, String uuid) {
		stateUuidMap.put(state, uuid);
	}

	private Map<Transition<T>, String> transitionUuidMap;

	protected void setTransitionUuidMap(
			Map<Transition<T>, String> transitionUuidMap) {
		this.transitionUuidMap = transitionUuidMap;
	}

	protected void putTransitionUuid(Transition<T> transition, String uuid) {
		transitionUuidMap.put(transition, uuid);
	}

	protected void registerEntryState(AbstractState<T> state) {
		entryState = state;
		addState(state);
	}

	@Override
	public void begin(StateChartSimIntegrator integrator) {
		if (entryState == null) {
			// illegal state
			throw new IllegalStateException(
					"An entry state was not registered in the StateChart.");
		}

		if (integrator.integrate(this)) {
			clearTransitions(null);
			List<AbstractState<T>> statesToEnter = getStatesToEnter(null, entryState);
			stateInit(statesToEnter);
		}
	}

	/**
	 * Stops this statechart.
	 */
	public void stop() {
		clearTransitions(null);
	}

	private void partitionQueueConsuming(List<Transition<T>> transitions,
			List<Transition<T>> queueConsuming, List<Transition<T>> nonQueueConsuming) {
		for (Transition<T> t : transitions) {
			if (t.isTriggerQueueConsuming())
				queueConsuming.add(t);
			else
				nonQueueConsuming.add(t);
		}
	}

	private void stateInit(List<AbstractState<T>> statesToEnter) {

		currentSimpleState = (SimpleState<T>) statesToEnter.get(statesToEnter
				.size() - 1);
		notifyChangeListeners();
		// Entering states from the top down
		for (AbstractState<T> as : statesToEnter) {
			as.onEnter();
		}

		// collect all relevant self transitions and initialize
		for (Transition<T> st : selfTransitions) {
			if (statesToEnter.contains(st.getSource())) {
				activeSelfTransitions.add(st);
				st.initialize(this);
			}
		}

		if (currentSimpleState instanceof FinalState) {
			clearTransitions(null);
		} else {

			Transition<T> validBranchTransition = null;
			// This will hold any branch state outgoing transitions for
			// special treatment
			List<Transition<T>> transitionsToIgnore = new ArrayList<Transition<T>>();

			if (currentSimpleState instanceof BranchState) {
				List<Transition<T>> branchCandidateTransitions = new ArrayList<Transition<T>>();
				for (Transition<T> t : regularTransitions) {
					if (currentSimpleState == t.getSource()) {
						branchCandidateTransitions.add(t);
					}
				}
				transitionsToIgnore.addAll(branchCandidateTransitions);

				Transition<T> defaultTransition = null;
				List<Transition<T>> trueBranchTransitions = new ArrayList<Transition<T>>();
				for (Transition<T> t : branchCandidateTransitions) {
					if (t.getTrigger() instanceof AlwaysTrigger) {
						defaultTransition = t;
					} else {
						// Check of transition condition
						// (should not be checked again below)
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
					validBranchTransition = defaultTransition;
				// makeRegularTransition(defaultTransition);
				else
					validBranchTransition = t;
				// makeRegularTransition(t);

			}
			// Get all new candidate transitions
			List<Transition<T>> newTransitionsToActivate = new ArrayList<Transition<T>>();
			for (Transition<T> t : regularTransitions) {
				if (!transitionsToIgnore.contains(t)
						&& statesToEnter.contains(t.getSource())) {
					newTransitionsToActivate.add(t);
				}
			}

			// List of zero time new candidate transitions
			List<Transition<T>> newTransitionsToActivateZeroTime = new ArrayList<Transition<T>>();
			// for new candidate transitions find all zeroTimeTransitions
			for (Transition<T> tt : newTransitionsToActivate) {
				if (tt.canTransitionZeroTime())
					newTransitionsToActivateZeroTime.add(tt);
			}

			// Partition new candidate zero time transitions into queue
			// consuming and non queue consuming
			List<Transition<T>> newTransitionsToActivateZeroTimeQueueConsuming = new ArrayList<Transition<T>>();
			List<Transition<T>> newTransitionsToActivateZeroTimeNonQueueConsuming = new ArrayList<Transition<T>>();
			partitionQueueConsuming(newTransitionsToActivateZeroTime,
					newTransitionsToActivateZeroTimeQueueConsuming,
					newTransitionsToActivateZeroTimeNonQueueConsuming);

			// Partition active regular transitions into queue
			// consuming and non queue consuming
			List<Transition<T>> activeRegularQueueConsuming = new ArrayList<Transition<T>>();
			List<Transition<T>> activeRegularNonQueueConsuming = new ArrayList<Transition<T>>();
			partitionQueueConsuming(activeRegularTransitions,
					activeRegularQueueConsuming, activeRegularNonQueueConsuming);

			// The triggered non queue consuming transitions
			// Include:
			// valid branch transition,
			// triggered non queue consuming active transitions,
			// true non queue consuming new transitions
			List<Transition<T>> nonQueueConsumingValid = new ArrayList<Transition<T>>();
			// Add the branch transition if found above
			if (validBranchTransition != null) {
				nonQueueConsumingValid.add(validBranchTransition);
			}
			// Add triggered non queue consuming active transitions
			for (Transition<T> t : activeRegularNonQueueConsuming) {
				if (t.isTransitionTriggered()) {
					nonQueueConsumingValid.add(t);
				}
			}
			// Add true non queue consuming new transitions
			for (Transition<T> t : newTransitionsToActivateZeroTimeNonQueueConsuming) {
				if (t.isTransitionConditionTrue()) {
					nonQueueConsumingValid.add(t);
				}
			}

			// collect all relevant regular transitions and initialize
			activeRegularTransitions.addAll(newTransitionsToActivate);
			for (Transition<T> ct : newTransitionsToActivate) {
				ct.initialize(this);
			}

			// Combine all queueConsuming candidates
			List<Transition<T>> allQueueConsuming = ListUtils.union(
					newTransitionsToActivateZeroTimeQueueConsuming,
					activeRegularQueueConsuming);

			// Valid queue consuming transitions
			List<Transition<T>> queueConsumingValid = new ArrayList<Transition<T>>();

			// if there are no queue consuming
			if (allQueueConsuming.isEmpty()) {
				// clear queue
				queue.clear();
			} else {
				boolean foundIsResolveNow = false;
				// Look for active regular queue consuming that
				// should resolve now
				for (Transition<T> t : activeRegularQueueConsuming) {
					if (t.isResolveNow()) {
						foundIsResolveNow = true;
						break;
					}
				}
				// If found "resolve now" active regular queue consuming
				// or if there are queue consuming new transitions then
				// consume queue
				if (foundIsResolveNow
						|| !newTransitionsToActivateZeroTimeQueueConsuming.isEmpty()) {

					// Find queue consuming candidate transitions
					while (true) {
						// For the newly activated, check that the condition is
						// true
						for (Transition<T> tt : newTransitionsToActivateZeroTimeQueueConsuming) {
							if (tt.isTransitionConditionTrue())
								queueConsumingValid.add(tt);
						}
						// For the previously active transitions, check that
						// they are triggered
						for (Transition<T> tt : activeRegularQueueConsuming) {
							if (tt.isTransitionTriggered())
								queueConsumingValid.add(tt);
						}
						if (queueConsumingValid.isEmpty()) {
							queue.poll();
							if (queue.isEmpty())
								break;
						} else {
							break;
						}
					}
				}
			}

			// Concatenate queue and non queue consuming candidates and choose
			// one
			Transition<T> t = chooseOneTransition(ListUtils.union(
					nonQueueConsumingValid, queueConsumingValid));
			// If a zero time transition was found, make that transition
			if (t != null) {
				// if chosen one is queue consuming
				if (t.isTriggerQueueConsuming()) {
					queue.poll();
				}
				makeRegularTransition(t);
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
	 * Returns a list of states to exit, in the order that they should be exited.
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

	public void resolve() {
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

		List<Transition<T>> queueConsumingRegularCandidates = new ArrayList<Transition<T>>();

		List<Transition<T>> allQueueConsumingActiveTransitions = ListUtils.union(
				queueConsumingActiveSelfTransitions,
				queueConsumingActiveRegularTransitions);

		// This is for the corner case when there is a self transition
		// and a regular transition that both are valid based on the same
		// message, but another regular transition is chosen over the
		// message based regular transition. This makes sure that the
		// message in the queue is consumed since the self transition
		// "used" it, even if the regular transition didn't.
		boolean queueConsumingSelfTransitionFollowed = false;

		// Are there no active self or regular queue consuming transitions?
		if (allQueueConsumingActiveTransitions.isEmpty()) {
			queue.clear();
		} else {
			boolean foundIsResolveNow = false;
			for (Transition<T> t : allQueueConsumingActiveTransitions) {
				if (t.isResolveNow()) {
					foundIsResolveNow = true;
					break;
				}
			}
			if (foundIsResolveNow) {
				while (true) {
					queueConsumingSelfTransitionFollowed = false;
					// Execute all queue consuming active self transitions
					for (Transition<T> t : getTriggeredTransitions(queueConsumingActiveSelfTransitions)) {
						t.onTransition();
						queueConsumingSelfTransitionFollowed = true;
					}

					// Look for queue consuming regular candidates for current
					// queue
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
			}
		}
		// Concatenate queue and non queue consuming candidates and choose one
		Transition<T> t = chooseOneTransition(ListUtils.union(
				nonQueueConsumingRegularCandidates, queueConsumingRegularCandidates));

		// reschedule selfTransitions
		rescheduleTransitions(activeSelfTransitions, false);

		// If a zero time transition was found, make that transition
		if (t != null) {
			// if chosen one is queue consuming
			if (t.isTriggerQueueConsuming() || queueConsumingSelfTransitionFollowed) {
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
		StateChartScheduler.INSTANCE.scheduleResolveTime(nextTime, this);
	}

	protected void removeResolveTime(double nextTime) {
		StateChartScheduler.INSTANCE.removeResolveTime(nextTime, this);
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

	public double getPriority() {
		return priority;
	}

	protected void setPriority(double priority) {
		this.priority = priority;
	}

	private T agent;

	public T getAgent() {
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
		if (s instanceof FinalState) {
			states.add(s);
			return states;
		}
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

	@Override
	public AbstractState<T> getStateForUuid(String uuid) {
		for (Entry<AbstractState<T>, String> entry : stateUuidMap.entrySet()) {
			if (entry.getValue().equals(uuid)) {
				return entry.getKey();
			}
		}
		return null;
	}

	@Override
	public Transition<T> getTransitionForUuid(String uuid) {
		for (Entry<Transition<T>, String> entry : transitionUuidMap.entrySet()) {
			if (entry.getValue().equals(uuid)) {
				return entry.getKey();
			}
		}
		return null;
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
		for (StateChartListener scl : stateChartListeners) {
			scl.update();
		}

	}

	@Override
	public void activateState(AbstractState<T> state) {
		SimpleState<T> simpleState = getCurrentSimpleState();
		// Check that there exists a current active simple state
		if (simpleState != null) {
			AbstractState<T> lca = null;
			// Being in a final state is like being in no state
			if (simpleState instanceof FinalState) {
				currentSimpleState = null;
			} else {
				lca = simpleState.calculateLowestCommonAncestor(state);
				List<AbstractState<T>> statesToExit = getStatesToExit(lca);
				currentSimpleState = null;
				for (AbstractState<T> as : statesToExit) {
					clearTransitions(as);
					as.onExit();
				}
			}

			List<AbstractState<T>> statesToEnter = getStatesToEnter(lca, state);

			stateInit(statesToEnter);
		}
		// TODO: consider activating the statechart if it's not active
	}

	@Override
	public void activateState(String stateID) {
		AbstractState<T> target = null;
		for (AbstractState<T> s : stateUuidMap.keySet()) {
			if (s != null) {
				String id = s.getId();
				if (id != null && id.equals(stateID)) {
					target = s;
					break;
				}
			}
		}
		if (target != null) {
			activateState(target);
		}
	}

	@Override
	public void followTransition(String transitionID) {
		Transition<T> target = null;
		for (Transition<T> t : transitionUuidMap.keySet()) {
			if (t != null) {
				String id = t.getId();
				if (id != null && id.equals(transitionID)) {
					target = t;
					break;
				}
			}
		}
		if (target != null) {
			followTransition(target);
		}
	}

	@Override
	public void followTransition(Transition<T> transition) {
		List<AbstractState<T>> currentStates = getCurrentStates();
		if (!currentStates.isEmpty()) {
			if (currentStates.contains(transition.getSource())) {
				if (transition instanceof SelfTransition) {
					transition.onTransition();
				} else {
					makeRegularTransition(transition);
				}
			}
		}

	}

}
