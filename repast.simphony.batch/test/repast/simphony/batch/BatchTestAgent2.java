package repast.simphony.batch;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;

import java.util.Set;

/**
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class BatchTestAgent2 {

	private String id;
	private Set results;

	public BatchTestAgent2(String id, Set results) {
		this.id = id;
		this.results = results;
	}

	public void step() {
		double tickCount = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		int runNumber = RunState.getInstance().getRunInfo().getRunNumber();
		results.add(id + ":" + runNumber + ":" + tickCount);
	}
}
