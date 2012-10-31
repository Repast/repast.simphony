package repast.simphony.statecharts;

public interface Trigger {

//	public boolean isTriggered();
	public void initialize(Transition t);
//	public void deactivate();
	public boolean isTriggered();
	public boolean isValid();
//	public void notifyTriggerListener();
	public boolean isRecurring();
	public double getInterval();
	public double getNextTime();
	public boolean canTransitionZeroTime();
}
