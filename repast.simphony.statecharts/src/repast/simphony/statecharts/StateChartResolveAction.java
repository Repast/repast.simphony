package repast.simphony.statecharts;

import java.util.HashMap;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.IAction;

public class StateChartResolveAction implements IAction {

	Map<StateChartResolveActionListener,Long> scralsCountsMap = new HashMap<StateChartResolveActionListener,Long>();

	// register listeners
	public void registerListener(StateChartResolveActionListener scral){
		if (!scralsCountsMap.containsKey(scral)){
			scralsCountsMap.put(scral, 0l);
		}
		long l = scralsCountsMap.get(scral);
		scralsCountsMap.put(scral, l + 1);
	}
	
	// remove listeners
	public void removeListener(StateChartResolveActionListener scral){
		if (scralsCountsMap.containsKey(scral)){
			long l = scralsCountsMap.get(scral);
			if (l <= 1){
				scralsCountsMap.remove(scral);
			}
			else {
				scralsCountsMap.put(scral, l - 1);
			}
		}
		
	}
	
	// notify listeners
	public void notifyListeners(){
		for(StateChartResolveActionListener scral : scralsCountsMap.keySet()){
			scral.resolve();
		}
	}
	
	public boolean hasListeners(){
		return !scralsCountsMap.isEmpty();
	}
	
	@Override
	public void execute() {
		notifyListeners();
		StateChartResolveActionScheduler.INSTANCE.clearOldResolveActions();
	}

}
