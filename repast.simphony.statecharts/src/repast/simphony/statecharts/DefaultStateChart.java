package repast.simphony.statecharts;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Uniform;

public class DefaultStateChart implements StateChart {

	public enum TransitionResolutionStrategy {
		RANDOM, NATURAL, PRIORITY
	}

	private TransitionResolutionStrategy trs = TransitionResolutionStrategy.RANDOM;

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

		// find immediate transition candidates
		Transition t;
		while (true) {
			List<Transition> zeroTimeTransitionCandidates = new ArrayList<Transition>();
			for (Transition tt : candidateTransitions) {
				if (tt.canTransitionZeroTime() && tt.isValid()) {
					zeroTimeTransitionCandidates.add(tt);
				}
			}
			t = chooseOneTransition(zeroTimeTransitionCandidates);
			if (t == null) {
				if (queue.isEmpty())
					break;
				else {
					queue.poll();
				}
			} else
				break;
		}
		if (t != null) {
			// if the transition consumes elements from queue, poll queue
			if (t.isTriggerQueueConsuming())
				queue.poll();
			makeRegularTransition(t);
		} else {
			// collect all relevant self transitions and initialize
			for (Transition st : selfTransitions) {
				if (st.getSource().getId().equals(state.getId())) {
					activeSelfTransitions.add(st);
					st.initialize(this);
				}
			}
			// collect all relevant regular transitions and initialize
			for (Transition ct : candidateTransitions) {
				if (ct.getSource().getId().equals(state.getId())) {
					activeRegularTransitions.add(ct);
					ct.initialize(this);
				}
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
		Transition transition = new Transition(trigger, state, state);
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

	private List<Transition> getTriggeredActiveTransitions(
			List<Transition> activeTransitions) {
		List<Transition> triggeredTransitions = new ArrayList<Transition>();
		for (Transition t : activeTransitions) {
			if (t.isTriggered())
				triggeredTransitions.add(t);
		}
		return triggeredTransitions;
	}

	public void resolve() {
		resolveSelfTransitions();
		resolveRegularTransitions();
	}

	private void resolveSelfTransitions() {
		List<Transition> triggeredTransitions = getTriggeredActiveTransitions(activeSelfTransitions);
		for (Transition t : triggeredTransitions) {
			t.onTransition();
		}
		// rescheduleSelfTransitions
		rescheduleTransitions(activeSelfTransitions);
	}

	private void resolveRegularTransitions() {
		List<Transition> triggeredTransitions = getTriggeredActiveTransitions(activeRegularTransitions);

		Transition t = chooseOneTransition(triggeredTransitions);
		// If there are no triggered transitions, reschedule
		if (t == null) {
			rescheduleTransitions(activeRegularTransitions);
			return;
		} else {
			if (t.isTriggerQueueConsuming())
				queue.poll();
			makeRegularTransition(t);
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
		switch (trs) {
		case NATURAL:
			return transitions.get(0);
		case PRIORITY:
			Collections.sort(transitions, pComp);
			return transitions.get(0);
		case RANDOM:
			int size = transitions.size();
			Uniform defaultUniform = RandomHelper.getUniform();
			int index = defaultUniform.nextIntFromTo(0, size - 1);
			return transitions.get(index);
		default:
			throw new IllegalArgumentException();
		}
	}

	private void rescheduleTransitions(List<Transition> activeTransitions) {
		// get current time
		double currentTime = RunEnvironment.getInstance().getCurrentSchedule()
				.getTickCount();
		// for each active transition
		for (Transition t : activeTransitions) {
			t.reschedule(this,currentTime);
			
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
		this.priority= priority;
	}

}
