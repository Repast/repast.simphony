package repast.simphony.batch;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;

import java.util.Set;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestAgent {

	private String id;
	private Set results;

	public BatchTestAgent(String id, Set results) {
		this.id = id;
		this.results = results;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		double tickCount = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		int runNumber = RunState.getInstance().getRunInfo().getRunNumber();
		results.add(id + ":" + runNumber + ":" + tickCount);
	}
}
