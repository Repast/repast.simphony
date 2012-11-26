package repast.simphony.statecharts;


public class HistoryState<T> extends AbstractState<T> {

	final boolean shallow;
	private AbstractState<T> destination;

	protected boolean isShallow() {
		return shallow;
	}

	protected AbstractState<T> getDestination() {
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

	protected void setDestination(AbstractState<T> destination) {
		this.destination = destination;
	}

	protected HistoryState(String id, boolean shallow) {
		super(id);
		this.shallow = shallow;
	}	
	

}
