package repast.simphony.statecharts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;

import org.apache.commons.collections15.ListUtils;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Uniform;

public class DefaultStateChart implements StateChart {

	private TransitionResolutionStrategy transitionResolutionStrategy = TransitionResolutionStrategy.RANDOM;

	@Override
	public TransitionResolutionStrategy getTransitionResolutionStrategy() {
		return transitionResolutionStrategy;
	}

	@Override
	public void setTransitionResolutionStrategy(
			TransitionResolutionStrategy transitionResolutionStrategy) {
		this.transitionResolutionStrategy = transitionResolutionStrategy;
	}

	private State entryState;
	// private List<State> states = new ArrayList<State>();

	// Regular transitions
	protected List<Transition> regularTransitions = new ArrayList<Transition>();
	protected List<Transition> activeRegularTransitions = new ArrayList<Transition>();

	// Self transitions
	protected List<Transition> selfTransitions = new ArrayList<Transition>();
	protected List<Transition> activeSelfTransitions = new ArrayList<Transition>();

	protected State currentState;

	@Override
	public void registerEntryState(State state) {
		entryState = state;
	}

	@Override
	public void begin() {
		if (entryState == null) {
			// illegal state
			throw new IllegalStateException(
					"An entry state was not registered in the StateChart.");
		}
		clearTransitionsAndCurrentState();
		entryState.onEnter();
		stateInit(entryState);
	}

	private void partitionQueueConsuming(List<Transition> transitions,
			List<Transition> queueConsuming, List<Transition> nonQueueConsuming) {
		for (Transition t : transitions) {
			if (t.isTriggerQueueConsuming())
				queueConsuming.add(t);
			else
				nonQueueConsuming.add(t);
		}
	}

	private void stateInit(State state) {
		currentState = state;

		// look through all defined regular transitions and find those with
		// source.id ==
		// state.id
		List<Transition> candidateTransitions = new ArrayList<Transition>();
		for (Transition t : regularTransitions) {
			if (t.getSource().getId().equals(state.getId())) {
				candidateTransitions.add(t);
			}
		}

		// Get zero time transitions
		List<Transition> zeroTimeTransitions = new ArrayList<Transition>();
		for (Transition tt : candidateTransitions) {
			if (tt.canTransitionZeroTime())
				zeroTimeTransitions.add(tt);
		}

		// Partition zero time transitions into queue consuming and non queue
		// consuming
		List<Transition> queueConsuming = new ArrayList<Transition>();
		List<Transition> nonQueueConsuming = new ArrayList<Transition>();
		partitionQueueConsuming(zeroTimeTransitions, queueConsuming,
				nonQueueConsuming);

		// Find non queue consuming candidate transitions
		List<Transition> nonQueueConsumingCandidates = new ArrayList<Transition>();
		for (Transition tt : nonQueueConsuming) {
			if (tt.isTransitionConditionTrue())
				nonQueueConsumingCandidates.add(tt);
		}

		List<Transition> queueConsumingCandidates = new ArrayList<Transition>();
		// Find queue consuming candidate transitions
		while (true) {
			for (Transition tt : queueConsuming) {
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

		// Concatenate queue and non queue consuming candidates and choose one
		Transition t = chooseOneTransition(ListUtils.union(
				nonQueueConsumingCandidates, queueConsumingCandidates));
		// If a zero time transition was found, make that transition
		if (t != null) {
			// if chosen one is queue consuming
			if (t.isTriggerQueueConsuming()) {
				queue.poll();
			}
			makeRegularTransition(t);
		}
		// Otherwise set up the self and regular transitions and initialize them
		else {
			// collect all relevant self transitions and initialize
			for (Transition st : selfTransitions) {
				if (st.getSource().getId().equals(state.getId())) {
					activeSelfTransitions.add(st);
					st.initialize(this);
				}
			}
			// collect all relevant regular transitions and initialize
			activeRegularTransitions.addAll(candidateTransitions);
			for (Transition ct : activeRegularTransitions) {
				ct.initialize(this);
			}

		}
	}

	private void clearTransitionsAndCurrentState() {
		// deactivate and clear all active transitions
		deactivateTransitions(activeSelfTransitions);
		deactivateTransitions(activeRegularTransitions);

		currentState = null;
	}

	private void deactivateTransitions(List<Transition> transitions) {
		double now = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		for (Transition t : transitions) {
			// if trigger's next time is after now,
			Trigger tr = t.getTrigger();
			double nextTime = tr.getNextTime();
			if (Double.compare(nextTime, now) > 0) {
				removeResolveTime(nextTime);
			}
		}
		transitions.clear();
	}

	@Override
	public void addState(State state) {
		// states.add(state);
	}

	@Override
	public void addRegularTransition(Transition transition) {
		regularTransitions.add(transition);
	}

	@Override
	public void addSelfTransition(Trigger trigger, State state) {
		addSelfTransition(trigger, Transition.createEmptyOnTransition(),
				Transition.createEmptyGuard(), state);
	}

	@Override
	public void addSelfTransition(Trigger trigger, Callable<Void> onTransition,
			Callable<Boolean> guard, State state) {
		Transition transition = new Transition(trigger, state, state);
		transition.registerOnTransition(onTransition);
		transition.registerGuard(guard);
		addSelfTransition(transition);
	}

	protected void addSelfTransition(Transition transition) {
		selfTransitions.add(transition);
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}

	private void makeRegularTransition(Transition transition) {
		State source = transition.getSource();
		State target = transition.getTarget();
		// check that target is not the same as source

		clearTransitionsAndCurrentState();
		source.onExit();

		// Transition action
		transition.onTransition();

		target.onEnter();

		stateInit(target);
	}

	private List<Transition> getTriggeredTransitions(
			List<Transition> transitions) {
		List<Transition> triggeredTransitions = new ArrayList<Transition>();
		for (Transition t : transitions) {
			if (t.isTransitionTriggered())
				triggeredTransitions.add(t);
		}
		return triggeredTransitions;
	}

	public void resolve() {
		// Partition active self transitions into queue consuming and non queue
		// consuming
		List<Transition> queueConsumingActiveSelfTransitions = new ArrayList<Transition>();
		List<Transition> nonQueueConsumingActiveSelfTransitions = new ArrayList<Transition>();
		partitionQueueConsuming(activeSelfTransitions,
				queueConsumingActiveSelfTransitions,
				nonQueueConsumingActiveSelfTransitions);

		// Execute all non queue consuming active self transitions
		for (Transition t : getTriggeredTransitions(nonQueueConsumingActiveSelfTransitions)) {
			t.onTransition();
		}

		// Partition active regular transitions into queue consuming and non
		// queue consuming
		List<Transition> queueConsumingActiveRegularTransitions = new ArrayList<Transition>();
		List<Transition> nonQueueConsumingActiveRegularTransitions = new ArrayList<Transition>();
		partitionQueueConsuming(activeRegularTransitions,
				queueConsumingActiveRegularTransitions,
				nonQueueConsumingActiveRegularTransitions);

		// Find non queue consuming candidate regular transitions
		List<Transition> nonQueueConsumingRegularCandidates = getTriggeredTransitions(nonQueueConsumingActiveRegularTransitions);
		List<Transition> queueConsumingRegularCandidates;
		while (true) {
			// Execute all queue consuming active self transitions
			for (Transition t : getTriggeredTransitions(queueConsumingActiveSelfTransitions)) {
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
		Transition t = chooseOneTransition(ListUtils.union(
				nonQueueConsumingRegularCandidates,
				queueConsumingRegularCandidates));
		// If a zero time transition was found, make that transition
		if (t != null) {
			// if chosen one is queue consuming
			if (t.isTriggerQueueConsuming()) {
				queue.poll();
			}
			makeRegularTransition(t);
		} else {
			// reschedule selfTransitions
			rescheduleTransitions(activeSelfTransitions,false);
			// reschedule regularTransitions
			rescheduleTransitions(activeRegularTransitions,true);
		}

	}

	private Transition chooseOneTransition(List<Transition> transitions) {
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
			List<Transition> temp = new ArrayList<Transition>(transitions);
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

	private void rescheduleTransitions(List<Transition> activeTransitions,
			boolean regular) {
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		// for each active transition
		if (regular) {
			for (Transition t : activeTransitions) {
				t.rescheduleRegularTransition(this, currentTime);

			}
		}
		else {
			for (Transition t : activeTransitions) {
				t.rescheduleSelfTransition(this, currentTime);
			}
		}
	}

	private Comparator<Transition> pComp = new PriorityComparator();

	/**
	 * Compares Transitions according to their priority. Lower priority later in
	 * order.
	 */
	static class PriorityComparator implements Comparator<Transition> {
		public int compare(Transition t1, Transition t2) {
			double index1 = t1.getPriority();
			double index2 = t2.getPriority();
			return index1 < index2 ? 1 : index1 == index2 ? 0 : -1;
		}
	}

	@Override
	public void scheduleResolveTime(double nextTime) {
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(nextTime,
				this);
	}

	@Override
	public void removeResolveTime(double nextTime) {
		StateChartResolveActionScheduler.INSTANCE.removeResolveTime(nextTime,
				this);
	}

	private Queue<Object> queue = new ArrayDeque<Object>();

	@Override
	public Queue<Object> getQueue() {
		return queue;
	}

	@Override
	public void receiveMessage(Object message) {
		queue.add(message);
	}

	private double priority = 0;

	@Override
	public double getPriority() {
		return priority;
	}

	@Override
	public void setPriority(double priority) {
		this.priority = priority;
	}

	@Override
	public void addBranch(Branch branch) {
		// create transition from "from" to branch
		branch.initializeBranch(this);
		// create into transition from ...
		IntoBranchTransition ibt = branch.getIntoBranchTransition();
		Transition t = new Transition(ibt.getFromTrigger(),
				ibt.getFrom(), branch, ibt.getFromTransitionPriority());
		t.registerGuard(ibt.getFromGuard());
		t.registerOnTransition(ibt.getFromOnTransition());
		addRegularTransition(t);
		
		// create tos transitions from ...
		List<OutOfBranchTransition> tos = branch.getTos();
		int numOfTos = tos.size();
		for (int i = 0; i < numOfTos; i++){
			OutOfBranchTransition oobt = tos.get(i);
			Transition outt = new Transition(oobt.getTrigger(), branch, oobt.getToState(), numOfTos - i);
			addRegularTransition(outt);
		}
	}

}
