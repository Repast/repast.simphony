package repast.simphony.statecharts;

import java.util.Queue;
import java.util.concurrent.Callable;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;


public class MessageTrigger extends AbstractTrigger{

	private final double pollingTime;
	private double initializedTickCount;
	private Queue<Object> queue;
	private MessageChecker messageChecker;
		
	public MessageTrigger(Queue<Object> queue, MessageChecker messageChecker, double pollingTime){
		this.queue = queue;
		this.messageChecker = messageChecker;
		this.pollingTime = pollingTime;		
	}
	
	public MessageTrigger(Queue<Object> queue, MessageChecker messageChecker){
		this(queue, messageChecker, 1);
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
		return Double.compare(now, getNextTime()) >= 0 && isValid() ;
	}
	
	public boolean isValid(){
		if (queue.isEmpty()) return false;
		else {
			return messageChecker.isValidMessage(queue.peek());
		}
	}
	
	public String toString(){
		return "MessageTrigger with pollingTime: " + pollingTime;
	}

	@Override
	public boolean canTransitionZeroTime() {
		return true;
	}
	
	@Override
	public boolean isQueueConsuming() {
		return true;
	}


	
}
