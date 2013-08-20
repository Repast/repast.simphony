package repast.simphony.statecharts;

import repast.simphony.parameter.Parameters;

public abstract class AbstractStateBuilder<T> {

	protected AbstractStateBuilder(String id) {
		this.id = id;
	}

	protected String id;

	protected StateAction<T> onEnter = new StateAction<T>() {
		@Override
		public void action(T agent, AbstractState<T> state, Parameters params) throws Exception {
		}
	};

	protected StateAction<T> onExit = new StateAction<T>() {
		@Override
		public void action(T agent, AbstractState<T> state, Parameters params) throws Exception {
		}
	};

	public void registerOnEnter(StateAction<T> onEnter) {
		this.onEnter = onEnter;
	}

	public void registerOnExit(StateAction<T> onExit) {
		this.onExit = onExit;
	}

	protected void setAbstractProperties(AbstractState<T> state) {
		state.registerOnEnter(onEnter);
		state.registerOnExit(onExit);
	}
}
