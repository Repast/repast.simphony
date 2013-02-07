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

	Set<DefaultStateChart<?>> scSet = new LinkedHashSet<DefaultStateChart<?>>();

	// register listeners
	public void registerListener(DefaultStateChart<?> sc) {
		scSet.add(sc);
	}

	// To be used by initialization routines
	public void removeAllListeners() {
		scSet.clear();
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
	protected void notifyListeners() {
		List<DefaultStateChart<?>> temp = new ArrayList<DefaultStateChart<?>>(scSet);
		SimUtilities.shuffle(temp, RandomHelper.getUniform());
		Collections.sort(temp, pComp);

		for (DefaultStateChart<?> sc : temp) {
			sc.begin();
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
