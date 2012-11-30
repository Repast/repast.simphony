package repast.simphony.statecharts;

import java.util.Queue;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ISchedule;


public class MessageTrigger<T> extends AbstractTrigger<T>{

	private Queue<Object> queue;

	protected Queue<Object> getQueue() {
		if (queue == null) {
			if (transition == null) {
				throw new IllegalStateException(
						"The transition was not set in a MessageTrigger.");
			} else {
				queue = transition.getQueue();
			}
		}
		return queue;
	}

	
	private final double pollingTime;
	private double initializedTickCount;
	private MessageChecker messageChecker;
		
	protected MessageTrigger(Queue<Object> queue, MessageChecker messageChecker, double pollingTime){
		this.queue = queue;
		this.messageChecker = messageChecker;
		this.pollingTime = pollingTime;
		if (messageChecker instanceof MessageConditionMessageChecker){
			
		}
	}
	
	protected MessageTrigger(Queue<Object> queue, MessageChecker messageChecker){
		this(queue, messageChecker, 1);
	}
	
	/**
	 * Proper constructor used for creating MessageTrigger.
	 * @param messageChecker
	 * @param pollingTime
	 */
	public MessageTrigger(MessageChecker messageChecker, double pollingTime){
		this(null, messageChecker, pollingTime);		
	}
	
	/**
	 * Proper constructor used for creating MessageTrigger.
	 * @param messageChecker
	 */
	public MessageTrigger(MessageChecker messageChecker){
		this(null, messageChecker, 1);		
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
		if (getQueue().isEmpty()) return false;
		else {
			return messageChecker.checkMessage(queue.peek());
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
