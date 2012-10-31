package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Uniform;

public class DefaultStateChart implements StateChart, TransitionListener {

	public enum TransitionResolutionStrategy {
		RANDOM, NATURAL, PRIORITY
	}

	private TransitionResolutionStrategy trs = TransitionResolutionStrategy.RANDOM;

	private State entryState;
	// private List<State> states = new ArrayList<State>();
	private List<Transition> transitions = new ArrayList<Transition>();
	private List<Transition> selfTransitions = new ArrayList<Transition>();
	
	private List<Transition> activeTransitions = new ArrayList<Transition>();

	private State currentState;

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
		//TODO: implement stateInit logic
		// look through all defined transitions and find those with source.id ==
		// state.id
		for (Transition t : transitions) {
			if (t.getSource().getId().equals(state.getId())) {
				activeTransitions.add(t);
				t.initialize(this);
			}
		}
		currentState = state;
	}

	private void clearTransitionsAndCurrentState() {
		// deactivate and clear all active transitions
		for (Transition t : activeTransitions) {
//			t.deactivate();
		}
		activeTransitions.clear();

		currentState = null;
	}

	@Override
	public void addState(State state) {
		// states.add(state);
	}

	@Override
	public void addTransition(Trigger trigger, State source, State target) {
		Transition transition = new Transition(trigger, source, target);
		transitions.add(transition);
	}
	
	@Override
	public void addSelfTransition(Trigger trigger, State state) {
		Transition transition = new Transition(trigger, state, state);
		selfTransitions.add(transition);
	}

	@Override
	public State getCurrentState() {
		return currentState;
	}

	@Override
	public void update(Transition transition) {
		boolean isSourceTarget = false;
		State source = transition.getSource();
		State target = transition.getTarget();
		// check that target is not the same as source
		if (source.getId().equals(target.getId())) {
			isSourceTarget = true;
		}
		if (!isSourceTarget) {
			source.onExit();
		}
		clearTransitionsAndCurrentState();
		// Transition action
		transition.onTransition();

		stateInit(target);
		if (!isSourceTarget) {
			target.onEnter();
		}
	}

	public List<Transition> getValidTransitions() {
		List<Transition> validTransitions = new ArrayList<Transition>();
		for (Transition t : activeTransitions) {
			if (t.isValid())
				validTransitions.add(t);
		}
		return validTransitions;
	}

	public void resolve() {
		resolveSelfTransitions();
		resolve(getValidTransitions());
	}

	private void resolveSelfTransitions() {
		// iterate through activeSelfTransitions
		// if valid execute onTransition
		
		
	}

	// TODO: include rescheduling logic if no valid transitions are found
	private void resolve(List<Transition> validTransitions) {
		// If there are no valid transitions, do nothing
		if (validTransitions.isEmpty())
			return;
		else {
			// If there is one valid transition, make the transition
			if (validTransitions.size() == 1)
				update(validTransitions.get(0));
			else {
				// Otherwise resolve based on the StateChart's
				// TransitionResolutionStrategy
				if (trs == TransitionResolutionStrategy.RANDOM) {
					int size = validTransitions.size();
					Uniform defaultUniform = RandomHelper.getUniform();
					int index = defaultUniform.nextIntFromTo(0, size - 1);
					update(validTransitions.get(index));
				} else if (trs == TransitionResolutionStrategy.NATURAL) {
					update(validTransitions.get(0));
				} else {
					Collections.sort(validTransitions, pComp);
					update(validTransitions.get(0));
				}
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
		StateChartResolveActionScheduler.INSTANCE.scheduleResolveTime(nextTime, this);
	}

}
