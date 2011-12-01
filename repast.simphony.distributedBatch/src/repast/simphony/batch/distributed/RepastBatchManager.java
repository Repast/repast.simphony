package repast.simphony.batch.distributed;

import java.util.Collection;
import java.util.List;


/**
 * Interface used for managing grid tasks.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 * 
 */
public interface RepastBatchManager {

	/**
	 * Method to start the grid connection
	 */
	public void startGrid();  
	
	/**
	 * Method to execute a given task with a list of jobs.
	 * @param task the task to execute
	 * @param jobs the jobs to execute for the task
	 */
	@SuppressWarnings("unchecked")
	public void executeTask(Class task,List<RepastJob>jobs);
	
	/**
	 * Method to close grid connection.
	 */
	public void finishUp();
	
	/**
	 * Method to return existing nodes
	 * @return collection of active nodes
	 */
	public Collection returnNodes();
	
}
