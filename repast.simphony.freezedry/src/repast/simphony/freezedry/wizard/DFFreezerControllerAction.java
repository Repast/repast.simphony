package repast.simphony.freezedry.wizard;

import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.freezedry.datasource.DelimitedFileDataSource;

public class DFFreezerControllerAction extends FreezerControllerAction {
	
	public DFFreezerControllerAction(ScheduleParameters scheduleParams, Object contextId, DelimitedFileDataSource dataSource) {
		super(scheduleParams, contextId, dataSource);
	}
	
	public DFFreezerControllerAction(ScheduleParameters scheduleParams, DelimitedFileDataSource dataSource) {
		super(scheduleParams, dataSource);
	}
	
	@Override
	public String toString() {
		return "Freeze Drying Action";
	}
}
