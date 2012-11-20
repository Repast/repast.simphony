package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class CompositeState<T> extends AbstractState<T> {
	
	private List<HistoryState<T>> historyStates = new ArrayList<HistoryState<T>>();

	public List<HistoryState<T>> getHistoryStates() {
		return historyStates;
	}

	public void addHistoryState(HistoryState<T> historyState){
		historyStates.add(historyState);
		historyState.setParent(this);
	}
	
	public void removeHistoryState(HistoryState<T> historyState){
		historyStates.remove(historyState);
	}
	
	public void removeAllHistoryStates(){
		historyStates.clear();
	}
	
	private AbstractState<T> entryState;
	
	public AbstractState<T> getEntryState() {
		return entryState;
	}

	private List<AbstractState<T>> children = new ArrayList<AbstractState<T>>();
	
	public void registerEntryState(AbstractState<T> state) {
		entryState = state;
		add(state);
	}
	
	public CompositeState(String id) {
		super(id);
	}
	
	public void add(AbstractState<T> state){
		state.setParent(this);
		if (!children.contains(state)){
			children.add(state);
		}
	}

}
