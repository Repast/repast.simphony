package repast.simphony.batch.distributed.gridgain;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.gridgain.grid.GridException;
import org.gridgain.grid.GridJob;
import org.gridgain.grid.GridJobAdapter;
import org.gridgain.grid.GridJobResult;
import org.gridgain.grid.GridNode;
import org.gridgain.grid.GridTaskSplitAdapter;

import repast.simphony.batch.distributed.BatchQueue;
import repast.simphony.batch.distributed.BatchQueueImpl;
import repast.simphony.batch.distributed.BatchRunResults;
import repast.simphony.batch.distributed.BatchTaskRun;
import repast.simphony.batch.distributed.Message;
import repast.simphony.batch.distributed.RepastJob;
import repast.simphony.batch.distributed.gridgain.GridGainBatchTask;
import repast.simphony.batch.distributed.gridgain.GridGainSetup;
import repast.simphony.batch.setup.BatchMainSetup;
import simphony.util.messages.MessageCenter;

/**
 * Grid task that defines how jobs are partitioned in the grid.
 * Users should override this class to create more specific task routing and
 * partitioning. This class simply splits jobs evenly into available nodes, launches processes on local 
 * and remote nodes, and returns both local and remote data, with the data placed in the local node's output folder.
 * 
 * @author Mark Altaweel
 * @version $Revision: 1.2
 * 
 * @param <T>
 */
@SuppressWarnings("serial")
public class GridGainBatchTask<T> extends GridTaskSplitAdapter<Object, Object>  implements BatchTaskRun{
	
    /**Batch Queue*/
    BatchQueue<RepastJob> queue;
    
    /**Grid Task Setup*/
    GridGainSetup setup;
    
    /**List of Threads to execute*/
    List<RepastJob> threads=new ArrayList<RepastJob>();
    
    /**Nodes that will listen for a message */
    private Map<String,GridNode>nodeMessanging;
    
    /**List of batch results from nodes */
	private List<BatchRunResults> multiRunResult=new ArrayList<BatchRunResults>();
	
	/**Name of the project distributed*/
	private String projectName;

//	private Message message;
    
    /**
     * Constructor used to add queue arguments (i.e., jobs ready to be processed)
     * @param queue the jobs queue
     */
	public GridGainBatchTask(BatchQueueImpl<RepastJob> queue){
		this.queue=queue;
    }
	
	/**
	 * Default constructor for current runs.
	 */
	public GridGainBatchTask(){
    }

	
	
    public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Map<String, GridNode> getNodeMessanging() {
		return nodeMessanging;
	}

	public void setNodeMessanging(Map<String, GridNode> nodeMessanging) {
		this.nodeMessanging = nodeMessanging;
	}

	@SuppressWarnings("unchecked")
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
    
    @Override
    public void setupMessanging(Message message){
    	GridMessage gm = new GridMessage();
    	GridGainSetup.Grid.addMessageListener(gm);

    	Iterator<GridNode>vi=nodeMessanging.values().iterator();
    	while(vi.hasNext()){
    		GridNode n  = vi.next();
    		try {
				GridGainSetup.Grid.sendMessage(n, message);
			} catch (GridException e) {
				e.printStackTrace();
			}
    	}
    }
   
    public void setMessangingNodes(Map messageMap){
    	this.nodeMessanging=messageMap;
    }
    
   	/**
   	 * Class defining the distributed job.
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

	@Override
	public T reduce(List<GridJobResult> arg0) throws GridException {
		Iterator<GridJobResult> ic = arg0.iterator();
		List<Map<GridNode,BatchRunResults>>resultOutputs 
			= new ArrayList<Map<GridNode,BatchRunResults>>();
		while(ic.hasNext()){
			Map<GridNode,BatchRunResults>outputs=new HashMap<GridNode,BatchRunResults>();
			GridJobResult r = ic.next();
			BatchRunResults br=r.getData();
			GridNode n=r.getNode();
		    outputs.put(n,br);
		    resultOutputs.add(outputs);
		}
		produceResults(resultOutputs);
		return null;
	}
	
	/**
	 * Method to get the output files from the nodes.
	 * @param resultOutputs the results from the nodes.
	 */
	public void produceResults(List<Map<GridNode,BatchRunResults>> resultOutputs){
		Iterator<Map<GridNode,BatchRunResults>> oi = resultOutputs.iterator();
		while(oi.hasNext()){
			Map<GridNode,BatchRunResults>results=oi.next();
			Iterator<GridNode> ic = results.keySet().iterator();

			Map<GridNode,String>have=new HashMap<GridNode,String>();
			while(ic.hasNext()){
				GridNode node = ic.next();
				BatchRunResults brr = results.get(node);
				if(brr==null)
					continue;
				Map<String,Object> result = brr.getResults();
				if(result==null)
					continue;
				Iterator<String>si = result.keySet().iterator();
			
				while(si.hasNext()){
					String f = si.next();
					Object output = (Object) result.get(f);
					if(!have.isEmpty() && have.containsKey(node) && have.get(node)==f)
						continue;
					FileOutputStream out=null;
					try {
						out = new FileOutputStream(new File(".."+File.separator+BatchMainSetup.ProjectName+File.separator+
							"output"+File.separator+f));
					} catch (IOException e) {
						MessageCenter.getMessageCenter(GridGainBatchTask.class).error("Problems with creating buffered writer.", e);
						e.printStackTrace();
					}
					try {
						out.write((byte[])output);
					} catch (IOException e) {
						MessageCenter.getMessageCenter(GridGainBatchTask.class).error("Problems with writing output data.", e);
						e.printStackTrace();
					}
					try {
						out.close();
					} catch (IOException e) {
						MessageCenter.getMessageCenter(GridGainBatchTask.class).error("Problems with closing buffered writer.", e);
						e.printStackTrace();
					}
			
					have.put(node, f);
				}
			}
		}
	}
	
	@Override
	public List<BatchRunResults> returnResults(){
		return multiRunResult;
	}

	@Override
	public void finishTask() {
		setup.finishUp();	
	}

	@Override
	public void startTask(BatchQueue<RepastJob> queue) {
		this.queue=queue;
		setup.executeTask(GridGainBatchTask.class, queue.getJobs());
		
	}

	@Override
	public void execute(Runnable command) {
		threads.add((RepastJob)command);
	}

	@Override
	public void initializeTask() {
		setup = new GridGainSetup();
		setup.startGrid();
	}
	
	public GridGainSetup getGridGainSetup(){
		return setup;
	}

	@Override
	public Collection returnNodeCollection() {
		return setup.returnNodes();
	}
}
