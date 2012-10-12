package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;

public class TimedTransition implements Transition {

	private final double time;
	private ISchedulableAction currentScheduledAction;
	
	public TimedTransition(double time){
		this.time = time;
	}
	
	private boolean triggered = false;
	
	public boolean isTriggered() {
		return triggered;
	}
	
	public void initializeTrigger(){
		triggered = false;
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double currentTickCount = schedule.getTickCount();
		schedule.removeAction(currentScheduledAction);
		currentScheduledAction = schedule.schedule(ScheduleParameters.createOneTime(currentTickCount + time), this, "trigger");
	}
	
	public void trigger(){
		this.triggered = true;
	}
	
	public void deactivateTrigger(){
		if (currentScheduledAction != null){
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
			schedule.removeAction(currentScheduledAction);
		}
	}

	
}
