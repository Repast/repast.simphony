package repast.simphony.statecharts;

public abstract class AbstractTrigger implements Trigger {

	TriggerListener triggerListener;

	protected void registerTriggerListener(TriggerListener triggerListener) {
		this.triggerListener = triggerListener;
	}

	

	protected void removeTriggerListener() {
		triggerListener = null;
	}

}