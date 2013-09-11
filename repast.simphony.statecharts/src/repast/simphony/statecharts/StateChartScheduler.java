package repast.simphony.statecharts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.PriorityType;
import repast.simphony.engine.schedule.ScheduleParameters;

/**
 * This is singleton responsible for managing the scheduling of statechart begin
 * and resolve actions. Both of these actions need to be managed because they
 * are added and removed based on statechart or simulation logic.
 * 
 * @author jozik
 * 
 */
public enum StateChartScheduler {

	INSTANCE;

	private final static long MAX_BEFOFE_CLEAR = 100;
	protected Map<Double, ResolveActionsMapValue> resolveActions = new HashMap<Double, ResolveActionsMapValue>();

	protected Map<Double, BeginActionsMapValue> beginActions = new HashMap<Double, BeginActionsMapValue>();
	protected DefaultIntegrator integrator = new DefaultIntegrator();

	/**
	 * Local class to hold resolve action information.
	 * 
	 * @author jozik
	 * 
	 */
	static class ResolveActionsMapValue {
		private StateChartResolveAction scra;
		private ISchedulableAction isa;
		private boolean remove = false;

		protected ResolveActionsMapValue(StateChartResolveAction scra,
				ISchedulableAction isa) {
			this.scra = scra;
			this.isa = isa;
		}

		protected void registerListener(StateChart<?> sc) {
			scra.registerListener(sc);
		}

		protected void removeListener(StateChart<?> sc) {
			scra.removeListener(sc);
			if (!scra.hasListeners()) {
				RunEnvironment.getInstance().getCurrentSchedule()
						.removeAction(isa);
				isa = null;
				scra = null;
				remove = true;
			}
		}

		protected void nullify() {
			scra.removeAllListeners();
			RunEnvironment.getInstance().getCurrentSchedule().removeAction(isa);
			isa = null;
			scra = null;
			remove = true;
		}

		protected boolean toRemove() {
			return remove;
		}
	}

	/**
	 * Local class to hold begin action information.
	 * 
	 * @author jozik
	 * 
	 */
	static class BeginActionsMapValue {
		private StateChartBeginAction scba;
		private ISchedulableAction isa;

		protected BeginActionsMapValue(StateChartBeginAction scba,
				ISchedulableAction isa) {
			this.scba = scba;
			this.isa = isa;
		}

		protected void registerListener(StateChart<?> sc) {
			scba.registerListener(sc);
		}

		protected void nullify() {
			scba.removeAllListeners();
			RunEnvironment.getInstance().getCurrentSchedule().removeAction(isa);
			isa = null;
			scba = null;
		}
	}

	/**
	 * Initializes the scheduler. This is automatically called by a simulation
	 * end action or for simulations using other forms of initialization during
	 * a simulation run, from initialization appropriate places if (e.g., ReLogo
	 * setup methods, via clearAll())
	 */
	public void initialize() {
		integrator.reset();
		shouldInitialize = false;
		resolveClearCounter = 0;
		beginClearCounter = 0;

		// remove resolveActions from schedule
		for (ResolveActionsMapValue ramv : resolveActions.values()) {
			ramv.nullify();
		}
		resolveActions.clear();

		for (BeginActionsMapValue bamv : beginActions.values()) {
			bamv.nullify();
		}
		beginActions.clear();
		RunEnvironment.getInstance().getCurrentSchedule()
				.schedule(ScheduleParameters.createAtEnd(0), new IAction() {

					@Override
					public void execute() {
						shouldInitialize = true;
					}

				});
	}

	long resolveClearCounter = 0;
	long beginClearCounter = 0;

	// called by StateChartResolveAction after notifying listeners
	// this allows for the rTime.compareTo(time) <= 0 expression
	// since the current time resolve actions have all been run
	protected void clearOldResolveActions() {
		resolveClearCounter++;
		if (resolveClearCounter > MAX_BEFOFE_CLEAR) {
			double time = RunEnvironment.getInstance().getCurrentSchedule()
					.getTickCount();
			List<Double> keysToRemove = new ArrayList<Double>();
			for (Double rTime : resolveActions.keySet()) {
				if (rTime.compareTo(time) <= 0)
					keysToRemove.add(rTime);
			}
			for (Double key : keysToRemove) {
				resolveActions.remove(key);
			}
			resolveClearCounter = 0;
		}
	}

	protected void clearOldBeginActions() {
		beginClearCounter++;
		if (beginClearCounter > MAX_BEFOFE_CLEAR) {
			double time = RunEnvironment.getInstance().getCurrentSchedule()
					.getTickCount();
			List<Double> keysToRemove = new ArrayList<Double>();
			for (Double rTime : beginActions.keySet()) {
				if (rTime.compareTo(time) <= 0)
					keysToRemove.add(rTime);
			}
			for (Double key : keysToRemove) {
				beginActions.remove(key);
			}
			beginClearCounter = 0;
		}
	}

	private boolean shouldInitialize = true;

	protected void scheduleResolveTime(double nextTime, StateChart<?> sc) {
		if (shouldInitialize) {
			initialize();
		}
		ResolveActionsMapValue ramv = resolveActions.get(nextTime);
		if (ramv == null) {
			ISchedule schedule = RunEnvironment.getInstance()
					.getCurrentSchedule();
			StateChartResolveAction scra = new StateChartResolveAction();
			ISchedulableAction ia = schedule.schedule(ScheduleParameters
					.createOneTime(nextTime, PriorityType.FIRST_OF_LAST), scra);
			ramv = new ResolveActionsMapValue(scra, ia);
			resolveActions.put(nextTime, ramv);

		}
		ramv.registerListener(sc);
	}

	/**
	 * Convenience method to schedule a statechart for immediate activation.
	 * 
	 * @param sc
	 *            statechart to be activated
	 */
	public static void beginNow(final StateChart<?> sc) {
		INSTANCE.scheduleBeginTime(0, sc);
	}

	/**
	 * Convenience method to schedule a statechart for activation at a later
	 * time.
	 * 
	 * @param later
	 *            ticks from current time when statechart should be activated
	 * @param sc
	 *            statechart to be activated
	 */
	public static void beginLater(double later, final StateChart<?> sc) {
		INSTANCE.scheduleBeginTime(later, sc);
	}

	/**
	 * Method to begin the statechart now without using the schedule. This is
	 * necessary for calling when the simulation is paused.
	 * 
	 * @param sc
	 */
	public void beginNowWithoutScheduling(StateChart<?> sc) {
		if (shouldInitialize) {
			initialize();
		}
		if (sc != null) {
			sc.begin(integrator);
		}
	}

	/**
	 * Called by generated statechart code to schedule the begin time for a
	 * statechart.
	 * 
	 * @param nextTime
	 * @param sc
	 */
	public void scheduleBeginTime(double nextTime, final StateChart<?> sc) {
		if (shouldInitialize) {
			initialize();
		}
		// If nextTime is negative, reset to 0
		if (nextTime < 0)
			nextTime = 0;

		double currentTickCount = RunEnvironment.getInstance()
				.getCurrentSchedule().getTickCount();
		// If currentTickCount is after nextTime, reinterpret nextTime to mean:
		// currentTickCount + nextTime
		if (Double.compare(currentTickCount, nextTime) > 0) {
			nextTime = currentTickCount + nextTime;
		}
		// If nextTime is the same as the currentTickCount
		// schedule for this time tick with PriorityType.FIRST
		if (Double.compare(currentTickCount, nextTime) == 0) {
			ISchedule schedule = RunEnvironment.getInstance()
					.getCurrentSchedule();
			// TODO: a reference to this ISchedulableAction might be needed for
			// descheduling the begin action
			// for the agent.
			ISchedulableAction isa = schedule.schedule(ScheduleParameters
					.createOneTime(currentTickCount, PriorityType.FIRST),
					new IAction() {
						@Override
						public void execute() {
							if (sc != null) {
								sc.begin(integrator);
							}
						}
					});

			// TODO: can get agent from statechart via: Object agent =
			// sc.getAgent();

		}
		// Otherwise, use the BeginActionsMapValue
		else {

			BeginActionsMapValue bamv = beginActions.get(nextTime);
			// If a BeginActionsMapValue doesn't exist for nextTime, create one
			if (bamv == null) {
				ISchedule schedule = RunEnvironment.getInstance()
						.getCurrentSchedule();
				StateChartBeginAction scba = new StateChartBeginAction(
						integrator);
				ISchedulableAction ia = schedule.schedule(ScheduleParameters
						.createOneTime(nextTime, PriorityType.FIRST), scba);
				bamv = new BeginActionsMapValue(scba, ia);
				beginActions.put(nextTime, bamv);
			}
			bamv.registerListener(sc);
			// TODO: need to figure out how to find this sc to removeListener if
			// necessary in the future
			// one idea is to keep data connecting reference "sc" to "nextTime"
			// then get appropriate bamv and removeListener(sc)
		}
	}

	// Called from deactivation of transitions in StateChart
	protected void removeResolveTime(double nextTime, StateChart<?> sc) {
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
