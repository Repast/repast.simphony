package repast.simphony.statecharts;

public abstract class AbstractTrigger implements Trigger {

	TriggerListener triggerListener;

	protected void registerTriggerListener(TriggerListener triggerListener) {
		this.triggerListener = triggerListener;
	}

	public void notifyTriggerListener() {
		triggerListener.update();
	}

	protected void removeTriggerListener() {
		triggerListener = null;
	}

}