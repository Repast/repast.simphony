package repast.simphony.statecharts;

public abstract class AbstractTrigger implements Trigger {

	@Override
	public boolean isQueueConsuming() {
		return false;
	}

}