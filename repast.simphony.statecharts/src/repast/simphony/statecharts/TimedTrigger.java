package repast.simphony.statecharts;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;


public class TimedTrigger extends AbstractTrigger{

	private final double time;
	private double initializedTickCount;
	private ISchedulableAction currentScheduledAction;
	
	public TimedTrigger(double time){
		this.time = time;
	}
	
	@Override
	public boolean isRecurring() {
		return false;
	}
	
	public double getInterval(){
		return 0;
	}
	
	public void initialize(Transition t){
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
	}
	
	@Override
	public double getNextTime() {
		return initializedTickCount + time;
	}
		
	public boolean isTriggered(){
		return isValid();
	}
	
	public boolean isValid(){
		return Double.compare(RunEnvironment.getInstance().getCurrentSchedule().getTickCount(),initializedTickCount + time) >= 0;
	}
	
	public String toString(){
		return "TimedTrigger with time: " + time;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return false;
	}





	
}
