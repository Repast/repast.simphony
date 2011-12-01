package repast.simphony.terracotta.datagrid;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import org.terracotta.masterworker.Worker;
import org.terracotta.workmanager.DefaultWorker;
import org.terracotta.workmanager.dynamic.DynamicWorkerFactory;

import repast.simphony.terracotta.datagrid.DataGridMaster;

/**
 * Main class to starting workers. Users should build off this class if 
 * modifications to different cluster typologies are needed.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 * 
 * @param <T>
 */
public class StartWorkers<T> {

	/**
	 * Method to start a worker from an array of worker ids.
	 * @param routingId routing id
	 * @throws Exception the exception thrown by DynamicWorkerFactory not starting
	 * properly
	 */
    @SuppressWarnings("unchecked")
	public List<Worker> startWorker(String[] routingIds) throws Exception {
        System.out.println("-- starting workers...");
        List<Worker>workers=new ArrayList<Worker>();
        for(int i=0; i < routingIds.length; i++){
        	Worker<?> worker=null;
        	String routingID = null;
        		if (routingIds.length!=0) {
        			routingID = routingIds[i];
            
        			worker = new DefaultWorker(DataGridMaster.DEFAULT_TOPOLOGY_NAME, DataGridMaster.DEFAULT_TOPOLOGY_FACTORY, routingID);
        		} else {
        			worker = new DynamicWorkerFactory(DataGridMaster.DEFAULT_TOPOLOGY_NAME, DataGridMaster.DEFAULT_TOPOLOGY_FACTORY, Executors.newSingleThreadExecutor()).
        			create();
        		}
        		worker.start();
        		workers.add(worker);
        }
//      Thread.currentThread().join();
        return workers;
    }
}
