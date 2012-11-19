package repast.simphony.statecharts;

import java.util.concurrent.Callable;

public class HistoryState extends AbstractState {

	final boolean shallow;
	private AbstractState destination;

	public boolean isShallow() {
		return shallow;
	}

	public AbstractState getDestination() {
		// History not established
		if (destination == null) {
			CompositeState parent = getParent();
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

	public void setDestination(AbstractState destination) {
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
	public void registerOnExit(Callable<Void> onExit) {
		throw new UnsupportedOperationException("HistoryStates are never exited, only entered. Use registerOnEnter instead.");
	}

}
