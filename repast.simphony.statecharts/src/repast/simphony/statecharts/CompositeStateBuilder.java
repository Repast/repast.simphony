package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import repast.simphony.parameter.IllegalParameterException;

public class CompositeStateBuilder<T> extends AbstractStateBuilder<T>{
	
	public CompositeStateBuilder(String id, AbstractState<T> entryState) {
		this(id, entryState, null);
	}
	
	public CompositeStateBuilder(String id, AbstractState<T> entryState, String entryStateUuid) {
		super(id);
		registerEntryState(entryState, entryStateUuid);
	}
	
	private List<AbstractState<T>> children = new ArrayList<AbstractState<T>>();
	private List<HistoryState<T>> historyStates = new ArrayList<HistoryState<T>>();
	private AbstractState<T> entryState;
	private Map<AbstractState<T>,String> stateUuidMap = new LinkedHashMap<AbstractState<T>,String>();
	
	public void addChildState(AbstractState<T> state){
		addChildState(state,null);
	}
	
	public void addChildState(AbstractState<T> state, String uuid){
		if (state instanceof HistoryState){
			throw new IllegalParameterException("Add HistoryStates using the addHistoryState(s) method(s).");
		}
		if (!children.contains(state)){
			children.add(state);
			if (uuid == null){
				uuid = UUID.randomUUID().toString();
			}
			stateUuidMap.put(state, uuid);
		}
	}
	
	public void addAllChildStates(List<AbstractState<T>> states){
		for (AbstractState<T> state : states){
			addChildState(state, null);
		}
	}
	
	private void registerEntryState(AbstractState<T> state, String uuid) {
		entryState = state;
		addChildState(state, uuid);
	}
	
	public void addHistoryState(HistoryState<T> historyState){
		addHistoryState(historyState,null);
	}
	
	public void addHistoryState(HistoryState<T> historyState, String uuid){
		if (!historyStates.contains(historyState)){
			historyStates.add(historyState);
			if (uuid == null){
				uuid = UUID.randomUUID().toString();
			}
			stateUuidMap.put(historyState, uuid);
		}
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
		
		// 
		result.setStateUuidMap(stateUuidMap);
		// Collect all state uuid mappings from composite states
		for (AbstractState<T> state : children){
			if (state instanceof CompositeState){
				CompositeState compositeState = (CompositeState)state;
				Map<AbstractState<T>,String> map = compositeState.getStateUuidMap();
				for(Entry<AbstractState<T>,String> entry : map.entrySet()){
					result.putStateUuid(entry.getKey(), entry.getValue());
				}
				compositeState.clearStateUuidMap();
			}
		}
		return result;
	}
	
}
