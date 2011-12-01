package repast.simphony.freezedry.engine;

import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.freezedry.wizard.FreezerControllerAction;
import repast.simphony.freezedry.datasource.JDBCDataSource;

public class JDBCFreezerControllerAction extends FreezerControllerAction {

	public JDBCFreezerControllerAction(ScheduleParameters scheduleParams, Object contextId, JDBCDataSource dataSource) {
		super(scheduleParams, contextId, dataSource);
	}

	public JDBCFreezerControllerAction(ScheduleParameters scheduleParams, JDBCDataSource dataSource) {
		super(scheduleParams, dataSource);
	}

}
