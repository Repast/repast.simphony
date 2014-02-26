package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.engine.schedule.IAction;
import repast.simphony.random.RandomHelper;
import repast.simphony.util.SimUtilities;

public class StateChartBeginAction implements IAction {

	Set<StateChart<?>> scSet = new LinkedHashSet<StateChart<?>>();
	private StateChartSimIntegrator integrator;
	
	public StateChartBeginAction(StateChartSimIntegrator integrator) {
	  this.integrator = integrator;
	}

	// register listeners
	public void registerListener(StateChart<?> sc) {
		if (sc != null){
			scSet.add(sc);
		}
	}
	
	// Remove specific listener (can be used, for example, for unscheduling
	// when agent is removed from context)
	public void removeListener(StateChart<?> sc) {
		if (sc != null){
			scSet.remove(sc);
		}
	}

	// To be used by initialization routines
	public void removeAllListeners() {
		scSet.clear();
	}

	private Comparator<StateChart<?>> pComp = new PriorityComparator();

	/**
	 * Compares StateCharts according to their priority. Lower priority later in
	 * order.
	 */
	static class PriorityComparator implements Comparator<StateChart<?>> {
		public int compare(StateChart<?> s1, StateChart<?> s2) {
			double index1 = ((StateChart<?>) s1).getPriority();
			double index2 = s2.getPriority();
			return index1 < index2 ? 1 : index1 == index2 ? 0 : -1;
		}
	}

	// notify listeners
	protected void notifyListeners() {
		List<StateChart<?>> temp = new ArrayList<StateChart<?>>(scSet);
		SimUtilities.shuffle(temp, RandomHelper.getUniform());
		Collections.sort(temp, pComp);

		for (StateChart<?> sc : temp) {
			sc.begin(integrator);
		}
	}

	public boolean hasListeners() {
		return !scSet.isEmpty();
	}

	@Override
	public void execute() {
		notifyListeners();
		StateChartScheduler.INSTANCE.clearOldBeginActions();
	}

}
