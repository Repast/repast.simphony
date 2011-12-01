package repast.simphony.engine.watcher;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class MyMultWatcher {

	 boolean triggered = false;
	 String watchID = "";
	 Generator generator;
	 int val = -1;

	@WatchItems(watches = {

	@Watch(id="watch1", watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query="linked 'network_1'",
          whenToTrigger = WatcherTriggerSchedule.IMMEDIATE),
	@Watch(id="watch2", watcheeClassName = "repast.simphony.engine.watcher.Generator", watcheeFieldNames = "counter", query="linked 'network_2'",
          whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)

		})
	public void triggered(WatchData data) {
		triggered = true;
		watchID = data.getID();
	}

	public void nonAnnotatedTrigger(WatchData data, Generator generator, int val) {
		triggered = true;
		watchID = data.getID();
		this.generator = generator;
		this.val = val;
	}

	public void reset() {
		triggered = false;
		watchID = "";
		generator = null;
		val = -1;
	}

	public String getWatchID() {
		return watchID;
	}

	public boolean isTriggered() {
		return triggered;
	}
}
