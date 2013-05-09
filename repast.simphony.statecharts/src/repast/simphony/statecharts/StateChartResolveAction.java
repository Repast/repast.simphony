package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

public class StateChartResolveAction implements IAction {

	Map<DefaultStateChart<?>,Long> scCountsMap = new LinkedHashMap<DefaultStateChart<?>,Long>();

	// register listeners
	public void registerListener(DefaultStateChart<?> sc){
		if (!scCountsMap.containsKey(sc)){
			scCountsMap.put(sc, 0l);
		}
		long l = scCountsMap.get(sc);
		scCountsMap.put(sc, l + 1);
	}
	
	// remove listeners
	public void removeListener(DefaultStateChart<?> sc){
		if (scCountsMap.containsKey(sc)){
			long l = scCountsMap.get(sc);
			if (l <= 1){
				scCountsMap.remove(sc);
			}
			else {
				scCountsMap.put(sc, l - 1);
			}
		}
		
	}
	
	// To be used by initialization routines
	public void removeAllListeners(){
		scCountsMap.clear();
	}
	
	
	
	private Comparator<DefaultStateChart<?>> pComp = new PriorityComparator();

	/**
	 * Compares StateCharts according to their priority. Lower priority later in
	 * order.
	 */
	static class PriorityComparator implements Comparator<DefaultStateChart<?>> {
		public int compare(DefaultStateChart<?> s1, DefaultStateChart<?> s2) {
			double index1 = ((DefaultStateChart<?>) s1).getPriority();
			double index2 = s2.getPriority();
			return index1 < index2 ? 1 : index1 == index2 ? 0 : -1;
		}
	}
	
	// notify listeners
	protected void notifyListeners(){
		List<DefaultStateChart<?>> temp = new ArrayList<DefaultStateChart<?>>(scCountsMap.keySet());
		SimUtilities.shuffle(temp, RandomHelper.getUniform());
		Collections.sort(temp,pComp);
		
		for(DefaultStateChart<?> sc : temp){
			sc.resolve();
		}
		scCountsMap.clear();
	}
	
	public boolean hasListeners(){
		return !scCountsMap.isEmpty();
	}
	
	@Override
	public void execute() {
		notifyListeners();
		StateChartScheduler.INSTANCE.clearOldResolveActions();
	}

}
