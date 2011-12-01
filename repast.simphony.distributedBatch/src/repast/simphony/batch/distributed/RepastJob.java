package repast.simphony.batch.distributed;

import java.io.Serializable;


/**
 * Interface for jobs distributed to the applied grid.
 * 
 * @version $Revision: 2.0
 * @author Mark Altaweel
 * 
 * @param <T>
 */
public interface RepastJob<T> extends Runnable, Serializable {

	/**
	 * Method to check to see if the job should be executed now.
	 * @return boolean
	 */
	public boolean executeNow();
	
	/**
	 * Method to return the results from an individual run,.
	 * @return
	 */
	public BatchRunResults returnResults();
	
	/**
	 * Method to add run results to a map for holding the simulation run's output.
	 * @param t1 a key used in the map.
	 * @param t2 a value object used in the map.
	 */
	public void addResult(Object t1, Object t2);
	
	/**
	 * Method to close any remaining processes and finish the run job.
	 */
	public void finishProcess();
	
	
}
