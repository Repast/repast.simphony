package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.parameter.IllegalParameterException;

public class CompositeStateBuilder<T> extends AbstractStateBuilder<T>{
	
	public CompositeStateBuilder(String id, AbstractState<T> entryState) {
		super(id);
		registerEntryState(entryState);
	}
	
	private List<AbstractState<T>> children = new ArrayList<AbstractState<T>>();
	private List<HistoryState<T>> historyStates = new ArrayList<HistoryState<T>>();
	private AbstractState<T> entryState;
	
	public void addChildState(AbstractState<T> state){
		if (state instanceof HistoryState){
			throw new IllegalParameterException("Add HistoryStates using the addHistoryState(s) method(s).");
		}
		if (!children.contains(state)){
			children.add(state);
		}
	}
	
	public void addAllChildStates(List<AbstractState<T>> states){
		for (AbstractState<T> state : states){
			addChildState(state);
		}
	}
	
	private void registerEntryState(AbstractState<T> state) {
		entryState = state;
		addChildState(state);
	}
	
	public void addHistoryState(HistoryState<T> historyState){
		historyStates.add(historyState);
	}
	
	public CompositeState<T> build(){
		if (entryState == null){
			throw new IllegalStateException("All CompositeStates must register an entry state.");
		}
		CompositeState<T> result = new CompositeState<T>(id);
		setAbstractProperties(result);
		result.registerEntryState(entryState);
		for (AbstractState<T> state : children){
			result.add(state);
		}
		for (HistoryState<T> state : historyStates){
			result.addHistoryState(state);
		}
		return result;
	}
	
}
