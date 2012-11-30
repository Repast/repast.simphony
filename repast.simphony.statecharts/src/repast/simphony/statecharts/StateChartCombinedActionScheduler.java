package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public enum StateChartCombinedActionScheduler {

	INSTANCE;

	private final static long MAX_BEFOFE_CLEAR = 10000;
	protected Map<Double, ResolveActionsMapValue> resolveActions = new HashMap<Double, ResolveActionsMapValue>();

	protected Map<Double, ResolveActionsMapValue> beginActions = new HashMap<Double, ResolveActionsMapValue>();

	static class ResolveActionsMapValue {
		private StateChartResolveAction scra;
		private ISchedulableAction isa;
		private boolean remove = false;

		protected ResolveActionsMapValue(StateChartResolveAction scra,
				ISchedulableAction isa) {
			this.scra = scra;
			this.isa = isa;
		}

		protected void registerListener(DefaultStateChart<?> sc) {
			scra.registerListener(sc);
		}

		protected void removeListener(DefaultStateChart<?> sc) {
			scra.removeListener(sc);
			if (!scra.hasListeners()) {
				RunEnvironment.getInstance().getCurrentSchedule()
						.removeAction(isa);
				isa = null;
				scra = null;
				remove = true;
			}
		}

		protected boolean toRemove() {
			return remove;
		}
	}

	public void initialize() {
		clearCounter = 0;
		resolveActions.clear();
//		for (Double key : resolveActions.keySet()) {
//			ResolveActionsMapValue ramv = resolveActions.remove(key);
//			ramv.isa = null;
//			ramv.scra = null;
//		}
//		resolveActions.clear();
	}

	long clearCounter = 0;

	protected void clearOldResolveActions() {
		clearCounter++;
		if (clearCounter > MAX_BEFOFE_CLEAR) {
			double time = RunEnvironment.getInstance().getCurrentSchedule()
					.getTickCount();
			List<Double> keysToRemove = new ArrayList<Double>();
			for (Double rTime : resolveActions.keySet()) {
				if (rTime.compareTo(time) <= 0)
					keysToRemove.add(rTime);
			}
			for (Double key : keysToRemove) {
				ResolveActionsMapValue ramv = resolveActions.remove(key);
//				ramv.isa = null;
//				ramv.scra = null;
			}
//			keysToRemove.clear();
			clearCounter = 0;
		}
	}

	protected void scheduleResolveTime(double nextTime, DefaultStateChart<?> sc) {
		ResolveActionsMapValue ramv = resolveActions.get(nextTime);
		if (ramv == null) {
			ISchedule schedule = RunEnvironment.getInstance()
					.getCurrentSchedule();
			StateChartResolveAction scra = new StateChartResolveAction();
			ISchedulableAction ia = schedule.schedule(ScheduleParameters
					.createOneTime(nextTime, ScheduleParameters.LAST_PRIORITY),
					scra);
			ramv = new ResolveActionsMapValue(scra, ia);
			resolveActions.put(nextTime, ramv);

		}
		ramv.registerListener(sc);
	}

	protected void removeResolveTime(double nextTime, DefaultStateChart<?> sc) {
		if (resolveActions.containsKey(nextTime)) {
			ResolveActionsMapValue ramv = resolveActions.get(nextTime);
			ramv.removeListener(sc);
			if (ramv.toRemove())
				resolveActions.remove(nextTime);
		} else {
			throw new IllegalStateException(
					"Excess removeResolveTime call detected for StateChart: "
							+ sc);
		}
	}

}
