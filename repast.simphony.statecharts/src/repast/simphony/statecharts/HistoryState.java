package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class HistoryState<T> extends AbstractState<T> {

	final boolean shallow;
	private AbstractState<T> destination;

	public boolean isShallow() {
		return shallow;
	}

	public AbstractState<T> getDestination() {
		// History not established
		if (destination == null) {
			CompositeState<T> parent = getParent();
			if (parent == null) {
				throw new IllegalStateException(
						"A history state cannot be at the root level of a state chart.");
			} else {
				return parent.getEntryState();
			}
		} else {
			return destination;
		}
	}

	public void setDestination(AbstractState<T> destination) {
		this.destination = destination;
	}

	public HistoryState(String id, boolean shallow) {
		super(id);
		this.shallow = shallow;
	}

	public HistoryState(String id) {
		this(id, true);
	}
	
	@Override
	public void registerOnExit(StateAction<T> onExit) {
		throw new UnsupportedOperationException("HistoryStates are never exited, only entered. Use registerOnEnter instead.");
	}

}
