package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class Transition implements TriggerListener {
	private Trigger trigger;
	private State source, target;

	public Transition(Trigger trigger, State source, State target) {
		this.trigger = trigger;
		this.source = source;
		this.target = target;
	}

	public Trigger getTrigger() {
		return trigger;
	}

	public State getSource() {
		return source;
	}

	public State getTarget() {
		return target;
	}

	public void deactivate() {
		removeTransitionListener();
		trigger.deactivate();
	}

	public void initialize(TransitionListener tl) {
		registerTransitionListener(tl);
		trigger.initialize(this);
	}

	TransitionListener transitionListener;

	protected void registerTransitionListener(TransitionListener tl) {
		transitionListener = tl;
	}

	protected void removeTransitionListener() {
		transitionListener = null;
	}

	@Override
	public void update() {
		// In the future, the guard checks may go here.
		transitionListener.update(this);
	}

	public void onTransition() {
		System.out.println("Making transition from: " + source.getId()
				+ " to: " + target.getId() + " via: " + trigger);
	}

}
