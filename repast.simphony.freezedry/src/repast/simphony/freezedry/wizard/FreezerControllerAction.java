package repast.simphony.freezedry.wizard;

import repast.simphony.engine.controller.SchedulingControllerAction;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.freezedry.ContextFreezeDryingAction;
import repast.simphony.freezedry.FreezeDryedDataSource;

public class FreezerControllerAction extends SchedulingControllerAction {

	public FreezerControllerAction(ScheduleParameters scheduleParams, Object contextId,
			FreezeDryedDataSource dataSource) {
		super(scheduleParams, new ContextFreezeDryingAction(contextId, dataSource));
	}

	public FreezerControllerAction(ScheduleParameters scheduleParams,
			FreezeDryedDataSource dataSource) {
		super(scheduleParams, new ContextFreezeDryingAction(dataSource));
	}

	public FreezeDryedDataSource getDataSource() {
		return getAction().getDataSource();
	}

	public ContextFreezeDryingAction getAction() {
		return (ContextFreezeDryingAction) super.action;
	}

	void setAction(IAction action) {
		super.action = action;
	}
}
