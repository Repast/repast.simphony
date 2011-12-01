package repast.simphony.batch.distributed.gridgain;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridConfiguration;
import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.GridDeploymentMode;
import org.gridgain.grid.GridException;
import org.gridgain.grid.GridFactory;
import org.gridgain.grid.GridTask;
import org.gridgain.grid.GridTaskFuture;
import org.gridgain.grid.GridTaskTimeoutException;
import org.gridgain.grid.marshaller.xstream.GridXstreamMarshaller;
import org.gridgain.grid.spi.collision.fifoqueue.GridFifoQueueCollisionSpi;

import repast.simphony.batch.distributed.RepastBatchManager;
import repast.simphony.batch.distributed.RepastJob;
import repast.simphony.batch.distributed.gridgain.GridGainSetup;
import simphony.util.messages.MessageCenter;

/**
 * Class for setting up grid functions. User may need to override this class
 * or build their own functionality extending GridGrain classes.
 * 
 * @author Mark Altaweel
 * @version $Revision: 1.2
 * @param <GridFutureTask>
 */
public class GridGainSetup<GridFutureTask> implements RepastBatchManager{
	
	/**The deployment configuration used*/
	private GridConfiguration config;

	/**The grid object used*/
	public static Grid Grid;  
	
	/**
	 * Default constructor to start distributed runs.
	 */
	public GridGainSetup(){	
		
	}
	
	/**
	 * Constructor with specified deployment configuration.
	 * @param config
	 */
	public GridGainSetup(GridConfiguration config){
		this.config=config;
	}
	

	@Override
	public void startGrid(){	
		GridXstreamMarshaller marshaller = new GridXstreamMarshaller();
		GridFifoQueueCollisionSpi spi = new GridFifoQueueCollisionSpi();
		spi.setParallelJobsNumber(1);
//		GridAdaptiveLoadBalancingSpi spi = new GridAdaptiveLoadBalancingSpi();  
//		GridAdaptiveProcessingTimeLoadProbe probe = new GridAdaptiveProcessingTimeLoadProbe(false); 
//		spi.setLoadProbe(probe);    
		GridConfigurationAdapter cfg = new GridConfigurationAdapter();
		cfg.setPeerClassLoadingEnabled(true);
		cfg.setDeploymentMode(GridDeploymentMode.SHARED);
		cfg.setCollisionSpi(spi);
		cfg.setMarshaller(marshaller);
		try {
			Grid=GridFactory.start(cfg);
		} catch (GridException e) {
			MessageCenter.getMessageCenter(GridGainSetup.class).error("GridGrain: Error, preparing Grid Grain execution." +
					"There is a grid exception preventing grid startup.", e);
		}	
	}
	
	public Collection returnNodes(){
		return Grid.getAllNodes();
	}
	
	/**
	 * Method to deploy a task on the grid nodes.
	 * @param task a class GridTask type
	 */
	@SuppressWarnings("unchecked")
	public void deployTask(Class<? extends GridTask> task){
		try {
			Grid.deployTask(task);
		} catch (GridException e) {
			MessageCenter.getMessageCenter(GridGainSetup.class).error("GridGrain: Error, preparing Grid Grain execution." +
				"There is a grid exception impeding deployement of task.", e);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void executeTask(Class task,List<RepastJob> jobs){
		try {
			Grid.execute(task,jobs).get();
		} catch (GridTaskTimeoutException e1) {
			MessageCenter.getMessageCenter(GridGainSetup.class).error("GridGrain: Error, preparing Grid Grain execution." +
				"There is a grid timeout exception.", e1);
		} catch (GridException e1) {
			MessageCenter.getMessageCenter(GridGainSetup.class).error("GridGrain: Error, preparing Grid Grain execution." +
				"There is a grid exception impeding execution.", e1);
		}	
	}
	
	/**
	 * This sets up future tasks to run on the cluster.
	 * @param task the task class type
	 * @param jobs the list of jobs to distribute
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public GridTaskFuture<?> executeFutureTask(Class task,List<RepastJob> jobs){
		GridTaskFuture<?> futureTask=null;
		futureTask=Grid.execute(task,jobs);
		
		return futureTask;
	}
	
	/**
	 * Method to undeploy a given task.
	 * @param task a grid task
	 */
	@SuppressWarnings("unchecked")
	public void undeployTask(Class<? extends GridTask> task){
		try {
			Grid.undeployTask(task.getCanonicalName());
		} catch (GridException e) {
			MessageCenter.getMessageCenter(GridGainSetup.class).error("GridGrain: Error, preparing Grid Grain execution." +
				"There is a grid exception impeding the undeployment of task.", e);
		} 
	}
	
	@Override
	public void finishUp(){
	    GridFactory.stopAll(true);  
	}

}
