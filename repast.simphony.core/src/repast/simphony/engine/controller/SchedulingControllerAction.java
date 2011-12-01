package repast.simphony.engine.controller;

import repast.simphony.engine.environment.DefaultControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.parameter.Parameters;

/**
 * This is a simple action that schedules an {@link IAction} in its
 * {@link #runInitialize(RunState, Object, Parameters)} method using a given
 * {@link ScheduleParameters}.
 * 
 * @author Jerry Vos
 */
public class SchedulingControllerAction extends DefaultControllerAction {

	protected IAction action;

	protected ScheduleParameters scheduleParams;

	/**
	 * Constructs this with the specified parameters and action to be scheduled
	 * during the sim's initialization.
	 * 
	 * @param scheduleParams
	 *            the schedule's parameters
	 * @param action
	 *            the action to schedule
	 */
	public SchedulingControllerAction(ScheduleParameters scheduleParams, IAction action) {
		super();
		this.action = action;
		this.scheduleParams = scheduleParams;
	}

	/**
	 * Schedule's the action with the schedule parameters on the schedule.
	 */
	@Override
	public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
		runState.getScheduleRegistry().getModelSchedule().schedule(scheduleParams, action);
	}

}