package repast.simphony.batch;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.watcher.Watch;
import repast.simphony.engine.watcher.WatcherTriggerSchedule;

import java.util.Set;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestAgent3 {

	String id;
	private Set results;
	private boolean fire = false;

	public BatchTestAgent3(String id, Set results) {
		this.id = id;
		this.results = results;
	}

	@Watch(watcheeClassName = "repast.simphony.batch.BatchTestAgent3", watcheeFieldNames = "fire", query = "linked_from",
	        whenToTrigger = WatcherTriggerSchedule.LATER, scheduleTriggerDelta = 1, scheduleTriggerPriority = 0)
	public void fire() {
		step();
		fire = !fire;
	}

	public void start() {
		fire();
	}

	public void step() {
		double tickCount = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		int runNumber = RunState.getInstance().getRunInfo().getRunNumber();
		results.add(id + ":" + runNumber + ":" + tickCount);
	}
}
