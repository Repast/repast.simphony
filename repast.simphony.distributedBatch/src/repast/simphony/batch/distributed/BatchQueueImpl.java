package repast.simphony.batch.distributed;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Default job queue implementation 
 * that contains distributable jobs.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 * @param <RepastJob>
 */
public class BatchQueueImpl<RepastJob> implements BatchQueue<RepastJob>, Iterable {

	/**List of jobs that will be distrubted*/
	List<RepastJob> jobs = new ArrayList<RepastJob>();
	
	@Override
	public void addJob(RepastJob arg1) {
		jobs.add(arg1);
	}

	@Override
	public void removeJob(RepastJob arg1) {
		jobs.remove(arg1);
	}

	@Override
	public Iterator<RepastJob> iterator() {
		return jobs.iterator();
	}
	
	public List<RepastJob> getJobs(){
		return jobs;
	}

}
