package repast.simphony.batch.distributed;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Interface for creating a Task that manages jobs to distribute.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 *
 */
public interface BatchTaskRun extends Executor {

	/**
	 * Start the task process by initializing an needed distributed solution.
	 */
	public void initializeTask();
	
	/**
	 * This starts the execution task for a defined job queue.
	 * @param queue the queue of distributed jobs
	 */
	public void startTask(BatchQueue<RepastJob> queue);

	
	/**
	 * Method to finish distributed task.
	 */
	public void finishTask();
	
	public void setProjectName(String name);

	public void setupMessanging(Message m);
	
	public void setMessangingNodes(Map messangingNodes);
	
	/**
	 * Method returns results from all the runs.
	 * @return the List of BatchRunResults.
	 */
	public List<BatchRunResults> returnResults();
	
	/**
	 * Return collection of active nodes
	 * @return collection of nodes
	 */
	public Collection returnNodeCollection();
	

}
