package repast.simphony.batch.distributed.gridgain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobAdapter;

import repast.simphony.batch.distributed.BatchRunResults;
import repast.simphony.batch.distributed.RepastJob;

/**
 * Class to probe nodes to setup batch tasks.
 * 
 * @version $Revision: 2.0
 * @author markaltaweel
 *
 */
public class ProbeBatchTask extends GridGainBatchTask {

	/**
	 * Default constructor
	 */
	public ProbeBatchTask(){
		super();
	}
	 
	@Override
	public Collection<? extends GridJob> split(int arg1, Object arg2) throws GridException{
	       List<RepastJob> threads = (List)arg2;
	        List<GridJob> jobs = new ArrayList<GridJob>(threads.size());

	        for (RepastJob j : threads) {
	            // jobs add here
	        	jobs.add(new GridRepastJob(j));
	        }    
	        return jobs;
		} 
	
	/**
	 * The inner class that executes the job.
	 * @author Mark Altaweel
	 */
	  private class GridRepastJob extends GridJobAdapter<Serializable> {
			private RepastJob job;

			/**
			 * Grid job that uses a repast object
			 * @param job a grid job
			 */
	        public GridRepastJob(RepastJob job){
	        	this.job=job;
	        }
	        
	        @Override
	        public Serializable execute() {
	            job.run();
	            job.finishProcess();
	            BatchRunResults br=job.returnResults();
	            return br;
	        }
	    }
}
