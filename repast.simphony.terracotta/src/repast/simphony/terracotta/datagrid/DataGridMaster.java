package repast.simphony.terracotta.datagrid;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.terracotta.message.routing.FixedRouter;
import org.terracotta.message.routing.LoadBalancingRouter;
import org.terracotta.message.routing.RoundRobinRouter;
import org.terracotta.message.routing.Router;
import org.terracotta.workmanager.dynamic.DynamicWorkManager;
import org.terracotta.workmanager.statik.StaticWorkManager;

import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;
import org.terracotta.message.pipe.BlockingQueueBasedPipe;
import org.terracotta.message.topology.DefaultTopology;
import org.terracotta.message.topology.Topology;

import simphony.util.messages.MessageCenter;

/**
 * Master class managing repast work items.
 * See http://www.terracotta.org/confluence/display/labs/Master+Worker 
 * for further details on similar implementations using Terracotta.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class DataGridMaster implements Runnable {
	
	/**The default topology used*/
    public static final String DEFAULT_TOPOLOGY_NAME = "topology";
    
    /**The default topology factory making an instance of BlockingQueueBasedPipe*/
    public static final Topology.Factory DEFAULT_TOPOLOGY_FACTORY = new DefaultTopology.Factory(
            new BlockingQueueBasedPipe.Factory(100));
    
    /**Share WorkCache object for storing work*/
    private final static WorkCache wCache = new SimpleWorkCache();    // the router
    
    /**The error message log for the object*/
	private MessageCenter LOG = MessageCenter.getMessageCenter(DataGridMaster.class);
	
    /**Work router*/
    private Router m_router;    
    
    /**The manager of processed work*/
    private WorkManager m_workManager;   
    
    /**Object for listening to work events*/
    private WorkListener m_workListener;
	
    /**List to store work*/
    private List<RepastWork> workItems;
	
    /**Number of completed attempts*/
    private int completedAttempts=0;
	
    /**Failure manager*/
    private StaticWorkManager m_failureManager;
	
	/**
	 * Constructor used to setup the master and routing mechanism applied.
	 * @param routingIDs the routing ids
	 * @param routingIDForFailOverNode the failure node
	 * @param workItems the work items
	 */
    public DataGridMaster(String[] routingIDs, String routingIDForFailOverNode,List<RepastWork>workItems) {
    	this.workItems=workItems;
        if (routingIDs == null) {
            m_router = new RoundRobinRouter();
            m_workManager = new DynamicWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router);
        } else if (routingIDs.length == 1) {
            // -- SINGLE WORK QUEUE - NO ROUTING - NO RETRY
            m_router = new FixedRouter(routingIDs[0]);
            m_workManager = new StaticWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, routingIDs);
        } else {
            // -- MULTIPLE WORK QUEUE - WITH ROUTING AND RETRY
            m_router = new LoadBalancingRouter();
           
            // RetryWorkListener(routingIDForFailOverNode);
            
            m_workManager = new StaticWorkManager("error"+DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, routingIDs);
        }
        m_failureManager=new StaticWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, 
        		routingIDForFailOverNode);
    }
    
    /**
     * Same as DataGridMaster(rountinIDs,routingIDForFailOverNode,workIems) 
     * except for the addition of a listener.
     * @param routingIDs
     * @param routingIDForFailOverNode
     * @param workItems the work applied
     * @param workListener a work listener
     */
    public DataGridMaster(String[] routingIDs, String routingIDForFailOverNode,List<RepastWork>workItems,
    		WorkListener workListener) {
    	this.workItems=workItems;
    	this.m_workListener=workListener;
    	
        if (routingIDs == null) {
            m_router = new RoundRobinRouter();
            m_workManager = new DynamicWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router);
        } else if (routingIDs.length == 1) {
            // -- SINGLE WORK QUEUE - NO ROUTING - NO RETRY
            m_router = new FixedRouter(routingIDs[0]);
            m_workManager = new StaticWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, routingIDs);
        } else {
            // -- MULTIPLE WORK QUEUE - WITH ROUTING AND RETRY
            m_router = new LoadBalancingRouter();
           
            // RetryWorkListener(routingIDForFailOverNode);
            
            m_workManager = new StaticWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, routingIDs);
        }
        m_failureManager=new StaticWorkManager(DEFAULT_TOPOLOGY_NAME, DEFAULT_TOPOLOGY_FACTORY, m_router, 
        		routingIDForFailOverNode);
    }

    /**
     * Method to cache work if needed for storage.
     * @param name the name/location to cache work
     * @param item the work item
     */
    public static void cacheWork(final String name, final RepastWork item) {
       wCache.setWork(name, item);
    }

    /**
     * Default run method to launch processes.
     */
    @SuppressWarnings("unchecked")
	public void run() {
        WorkItem workItem=null;
        
       for(int i=0; i<workItems.size();i++){
        	
        	try {
        		// schedule first work
        		workItem = scheduleWork(workItems.get(i));
        	} catch (WorkException e) {
        		MessageCenter.getMessageCenter(DataGridMaster.class).error("Work Item exception, check objects sent for work", e);
        		return;
        	}
        	
        final Set<WorkItem> pendingWork = new HashSet<WorkItem>();
        pendingWork.add(workItem);
        
        Set<WorkItem>notCompleted=new HashSet<WorkItem>();
        
        // loop while there still is pending work to wait for
        while (!pendingWork.isEmpty()) {

            // wait for any work that is completed
            Collection<WorkItem> completedWork=null;
            try {
                completedWork = m_workManager.waitForAny(pendingWork, WorkManager.INDEFINITE);
            } catch (InterruptedException e) {
                LOG.error("Terracotta: There is an interruption exception", 
                		e); // bail out
            }
            
            // loop over all completed work
            for (WorkItem workI : completedWork) {
                

                // check work status (completed or rejected)
                switch (workI.getStatus()) {
                    case WorkEvent.WORK_COMPLETED:
                        
                        // if completed - grab the result
                        RepastWork work = null;
						try {
							work = ((RepastWork) workI.getResult());
						} catch (WorkException e) {
							LOG.error("Problem getting work result",e);
						}
						
						//check related work and process
						if(work.returnRelatedWork()!=null &&
							!work.returnRelatedWork().isEmpty()){
							processAdditionalWork(work.returnRelatedWork(),
									pendingWork);
						}
                        // remove work from the pending list
                        pendingWork.remove(workI);
                        break;

                    case WorkEvent.WORK_REJECTED:
                        // work rejected - just remove the work
                        pendingWork.remove(workI);
                        break;
                        
                    case WorkEvent.WORK_STARTED:
                    	// work is started but not finished
                    	notCompleted.add(workI);
                    	break;
                    	
                    default:
                        // status is none of the above - should never happen
                        LOG.error("WorkItem is in unexpected state: ", new RepastWorkError(m_failureManager));
                    
                    	//just remove work    
                    	pendingWork.remove(workI);
                }
            }
        }
        
        //launch unfinished
       if(!notCompleted.isEmpty()) {
        	notCompleted=this.run(notCompleted);
        }
      }
    }
    
    /**
     * Run non-completed work originating from a work list.
     * @param pendingWork work not completed
     * @return a list of uncompleted work
     */
    @SuppressWarnings("unchecked")
	public Set<WorkItem> run(Set<WorkItem>pendingWork){
        // loop while there still is pending work to wait for
    	
    	
        while (!pendingWork.isEmpty()) {

            // wait for any work that is completed
            Collection<WorkItem> completedWork;
            try {
                completedWork = m_workManager.waitForAny(pendingWork, WorkManager.INDEFINITE);
            } catch (InterruptedException e) {
                throw new RuntimeException(e); // bail out
            }
            
            Iterator<WorkItem> ic = completedWork.iterator();
            while (ic.hasNext()) {
            	
            	WorkItem workI = ic.next();
                
            	// check work status (completed or rejected)
                switch (workI.getStatus()) {
                    case WorkEvent.WORK_COMPLETED:
                        
                        // if completed - grab the result
                        RepastWork work = null;
						try {
							work = ((RepastWork) workI.getResult());
						} catch (WorkException e) {
							LOG.error("Problem getting work result",e);
						}
						
						//check related work and process
						if(work.returnRelatedWork()!=null ||
								!work.returnRelatedWork().isEmpty()){
								processAdditionalWork(work.returnRelatedWork(),
										pendingWork);
						}
						
                        // remove work from the pending list
                        pendingWork.remove(workI);
                        completedAttempts=0;
                        break;

                    case WorkEvent.WORK_REJECTED:
                        // work rejected - just remove the work
                        pendingWork.remove(workI);
                        completedAttempts=0;
                        break;
                        
                    case WorkEvent.WORK_STARTED:
                    	// work is started but not finished
                    	for(completedAttempts=0;completedAttempts<100; completedAttempts++){
                    		try {
								m_workManager.schedule(workI.getResult());
							} catch (IllegalArgumentException e) {
								LOG.error("Problem with work argument",e);
							} catch (WorkException e) {
								LOG.error("Problem getting work result",e);
							}	
                    		pendingWork.add(workI);
                    	}
                    	break;
                    	
                    default:
                    	LOG.error("WorkItem is in unexpected state: ", new RepastWorkError(m_failureManager));
                 
                        pendingWork.remove(workI);
                        completedAttempts=0;
                }
            }
        }   
        return pendingWork;
    }
    
    /**
     * Schedule additional related work to current work.
     * @param workToProcess work related to current work
     * @param pendingWork overall pending work list
     */
    private void processAdditionalWork(List<RepastWork> workToProcess, final Set<WorkItem> pendingWork) {
        
        for (RepastWork additionalWork : workToProcess) {

              try {
                    // schedule work for each additional related work that is found
                    WorkItem newWorkItem = scheduleWork(additionalWork);

                    // add the new item to pending work list
                    pendingWork.add(newWorkItem);
                } catch (WorkException e) {
                    MessageCenter.getMessageCenter(DataGridMaster.class).
                    	error("Can't process additional work", e);
                    continue; 
                }
            }
    }

    /**
     * Schedule work activity.
     * @param a work item
     * @return work item
     * @throws WorkException
     */
    private WorkItem scheduleWork(RepastWork workItem) throws WorkException {
        try {
            // schedule the work in the work manager
            return m_workManager.schedule(workItem, m_workListener);
        } catch(Exception e) {
           MessageCenter.getMessageCenter(DataGridMaster.class).error("Problems with executing work item", e);
        }
		return null;
    }

	public List<RepastWork> getWorkItems() {
		return workItems;
	}

	public void setWorkItems(List<RepastWork> workItems) {
		this.workItems = workItems;
	}

	public WorkListener getM_workListener() {
		return m_workListener;
	}

	public void setM_workListener(WorkListener listener) {
		m_workListener = listener;
	}
}
