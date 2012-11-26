package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class CompositeState<T> extends AbstractState<T> {
	
	private List<HistoryState<T>> historyStates = new ArrayList<HistoryState<T>>();

	protected List<HistoryState<T>> getHistoryStates() {
		return historyStates;
	}

	protected void addHistoryState(HistoryState<T> historyState){
		historyStates.add(historyState);
		historyState.setParent(this);
	}
		
	private AbstractState<T> entryState;
	
	protected AbstractState<T> getEntryState() {
		return entryState;
	}

	private List<AbstractState<T>> children = new ArrayList<AbstractState<T>>();
	
	protected void registerEntryState(AbstractState<T> state) {
		entryState = state;
		add(state);
	}
	
	protected void add(AbstractState<T> state){
		state.setParent(this);
		if (!children.contains(state)){
			children.add(state);
		}
	}
	
	protected CompositeState(String id) {
		super(id);
	}

}
