package repast.simphony.statecharts;

public interface Trigger {

//	public boolean isTriggered();
	public void initialize(TriggerListener tl);
	public void deactivate();
	public void notifyTriggerListener();
}
