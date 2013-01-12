package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompositeState<T> extends AbstractState<T> {

	private List<HistoryState<T>> historyStates = new ArrayList<HistoryState<T>>();

	protected List<HistoryState<T>> getHistoryStates() {
		return historyStates;
	}

	protected void addHistoryState(HistoryState<T> historyState) {
		historyStates.add(historyState);
		historyState.setParent(this);
	}

	private AbstractState<T> entryState;

	protected AbstractState<T> getEntryState() {
		return entryState;
	}

	protected List<AbstractState<T>> children = new ArrayList<AbstractState<T>>();

	protected void registerEntryState(AbstractState<T> state) {
		entryState = state;
		add(state);
	}

	protected void add(AbstractState<T> state) {
		state.setParent(this);
		if (!children.contains(state)) {
			children.add(state);
		}
	}

	protected CompositeState(String id) {
		super(id);
	}

	// For building only
	private Map<AbstractState<T>, String> stateUuidMap;

	// For building only
	protected Map<AbstractState<T>, String> getStateUuidMap() {
		return stateUuidMap;
	}
	
	// For building only
	// Clear map after use in building.
	protected void clearStateUuidMap(){
		stateUuidMap.clear();
	}

	// For building only
	protected void setStateUuidMap(Map<AbstractState<T>, String> stateUuidMap) {
		this.stateUuidMap = stateUuidMap;
	}

	// For building only
	protected void putStateUuid(AbstractState<T> state, String uuid) {
		stateUuidMap.put(state, uuid);
	}
	


}
