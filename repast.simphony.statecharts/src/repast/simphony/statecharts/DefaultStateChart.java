package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.statecharts.State;
import repast.simphony.statecharts.StateChart;
import repast.simphony.statecharts.TimedTrigger;
import repast.simphony.statecharts.Transition;
import repast.simphony.statecharts.TransitionListener;
import repast.simphony.statecharts.Trigger;

public class DefaultStateChart implements StateChart, TransitionListener {

	private State entryState;
//	private List<State> states = new ArrayList<State>();
	private List<Transition> transitions = new ArrayList<Transition>();

	private List<Transition> activeTransitions = new ArrayList<Transition>();

	private State currentState;
	
	@Override
	public void registerEntryState(State state) {
		entryState = state;
	}

	@Override
	public void begin() {
		if (entryState == null){
			// illegal state
			throw new IllegalStateException("An entry state was not registered in the StateChart.");
		}
		clearTransitionsAndCurrentState();
		preEnter(entryState);
		entryState.onEnter();
	}

	private void preEnter(State state) {
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
			t.deactivate();
		}
		activeTransitions.clear();

		currentState = null;
	}

	@Override
	public void addState(State state) {
//		states.add(state);
	}

	@Override
	public void addTransition(Trigger trigger, State source, State target) {
		Transition transition = new Transition(trigger, source, target);
		transitions.add(transition);
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
		
		preEnter(target);
		if (!isSourceTarget) {
			target.onEnter();
		}
	}
	
	public List<Transition> getValidTransitions(){
		List<Transition> validTransitions = new ArrayList<Transition>();
		for(Transition t : activeTransitions){
			if(t.isValid()) validTransitions.add(t);
		}
		return validTransitions;
	}

	

	

}
