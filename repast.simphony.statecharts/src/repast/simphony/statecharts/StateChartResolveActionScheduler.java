package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public enum StateChartResolveActionScheduler {
	
	INSTANCE;
	
	protected Map<Double, ResolveActionsMapValue> resolveActions = new HashMap<Double, ResolveActionsMapValue>();

	static class ResolveActionsMapValue {
		private StateChartResolveAction scra;
		private ISchedulableAction isa;
		private boolean remove = false;
		
		public ResolveActionsMapValue(StateChartResolveAction scra,
				ISchedulableAction isa) {
			this.scra = scra;
			this.isa = isa;
		}
		
		public void registerListener(StateChartResolveActionListener scral){
			scra.registerListener(scral);
		}
		
		public void removeListener(StateChartResolveActionListener scral){
			scra.removeListener(scral);
			if (!scra.hasListeners()){
				RunEnvironment.getInstance().getCurrentSchedule().removeAction(isa);
				isa = null;
				scra = null;
				remove = true;
			}
		}
		
		public boolean toRemove(){
			return remove;
		}
	}
	
	public void clearOldResolveActions(){
		double time = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		List<Double> keysToRemove = new ArrayList<Double>();
		for(Double rTime: resolveActions.keySet()){
			if (rTime.compareTo(time) <= 0) keysToRemove.add(rTime);
		}
		for(Double key : keysToRemove) {
			ResolveActionsMapValue ramv = resolveActions.remove(key);
			ramv.isa = null;
			ramv.scra = null;
		}
	}
	
	public void scheduleResolveTime(double nextTime, StateChart sc) {
		// check if nextTime is already scheduled for resolving
		if (!resolveActions.containsKey(nextTime)) {
			ISchedule schedule = RunEnvironment.getInstance()
					.getCurrentSchedule();
			StateChartResolveAction scra = new StateChartResolveAction();
			ISchedulableAction ia = schedule.schedule(ScheduleParameters
					.createOneTime(nextTime, ScheduleParameters.LAST_PRIORITY),scra);
			ResolveActionsMapValue ramv = new ResolveActionsMapValue(scra, ia);
			resolveActions.put(nextTime, ramv);
		}
		ResolveActionsMapValue ramv = resolveActions.get(nextTime);
		ramv.registerListener(sc);
	}
	
	public void removeResolveTime(double nextTime, StateChart sc) {
		if (resolveActions.containsKey(nextTime)){
			ResolveActionsMapValue ramv = resolveActions.get(nextTime);
			ramv.removeListener(sc);
			if(ramv.toRemove()) resolveActions.remove(nextTime);
		}
	}
	
	
}
