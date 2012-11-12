package repast.simphony.statecharts;

import java.util.concurrent.Callable;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;


public class ConditionTrigger extends AbstractTrigger{

	private final double pollingTime;
	private double initializedTickCount;
	private Callable<Boolean> condition;
	
	public ConditionTrigger(Callable<Boolean> condition, double pollingTime){
		this.pollingTime = pollingTime;
		this.condition = condition;
	}
	
	public ConditionTrigger(Callable<Boolean> condition){
		this(condition,1);
	}
	
	@Override
	public boolean isRecurring() {
		return true;
	}
	
	public double getInterval(){
		return pollingTime;
	}
	
	public void initialize(){
		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		initializedTickCount = schedule.getTickCount();
	}
	
	@Override
	public double getNextTime() {
		return initializedTickCount + pollingTime;
	}
		
	public boolean isTriggered(){
		double now = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		return Double.compare(now, getNextTime()) >= 0 && isTriggerConditionTrue() ;
	}
	
	public boolean isTriggerConditionTrue(){
		boolean result = false;
		try {
			result = condition.call();
		} catch (Exception e) {
			MessageCenter.getMessageCenter(getClass()).error("Error encountered when calling condition: " + condition + " in " + this, e);
		}
		return result;
	}
	
	public String toString(){
		return "ConditionTrigger with pollingTime: " + pollingTime;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return true;
	}

	@Override
	public boolean isQueueConsuming() {
		return false;
	}





	
}
