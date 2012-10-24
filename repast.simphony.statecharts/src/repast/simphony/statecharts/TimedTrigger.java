package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;


public class TimedTrigger extends AbstractTrigger{

	private final double time;
	private ISchedulableAction currentScheduledAction;
	
	public TimedTrigger(double time){
		this.time = time;
	}
	
	public void initialize(TriggerListener tl){
		registerTriggerListener(tl);
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		double currentTickCount = schedule.getTickCount();
		if (currentScheduledAction != null){
			schedule.removeAction(currentScheduledAction);
		}
		currentScheduledAction = schedule.schedule(ScheduleParameters.createOneTime(currentTickCount + time), this, "trigger");
		
	}
	
	public void trigger(){
		notifyTriggerListener();
	}
	
	public void deactivate(){
		if (currentScheduledAction != null){
			ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
			schedule.removeAction(currentScheduledAction);
		}
		removeTriggerListener();
	}
	
	public String toString(){
		return "TimedTrigger with time: " + time;
	}

	
}
