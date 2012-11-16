package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

public class CompositeState extends AbstractState {
	
	private List<HistoryState> historyStates = new ArrayList<HistoryState>();

	public List<HistoryState> getHistoryStates() {
		return historyStates;
	}

	public void addHistoryState(HistoryState historyState){
		historyStates.add(historyState);
	}
	
	public void removeHistoryState(HistoryState historyState){
		historyStates.remove(historyState);
	}
	
	public void removeAllHistoryStates(){
		historyStates.clear();
	}
	
	private AbstractState entryState;
	
	public AbstractState getEntryState() {
		return entryState;
	}

	private List<AbstractState> children = new ArrayList<AbstractState>();
	
	public void registerEntryState(AbstractState state) {
		entryState = state;
		add(state);
	}
	
	public CompositeState(String id) {
		super(id);
	}
	
	public void add(AbstractState state){
		state.setParent(this);
		if (!children.contains(state)){
			children.add(state);
		}
	}

}
