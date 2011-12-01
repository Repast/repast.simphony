package repast.simphony.batch.distributed;

import java.util.List;


/** 
 * Repast distributed job queue.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 * @param <RepastJob>
 */
public interface BatchQueue<RepastJob> extends Iterable{
	
	/**
	 * Add a job to the queue.
	 * @param arg1
	 * @return
	 */
	public void addJob(RepastJob arg1);
	
	/**
	 * Remove a job from the queue.
	 * @param arg1
	 * @return
	 */
	public void removeJob(RepastJob arg1);
	
	public List<RepastJob> getJobs();

}
