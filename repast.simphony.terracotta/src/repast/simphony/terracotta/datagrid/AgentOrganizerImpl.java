package repast.simphony.terracotta.datagrid;

import java.util.List;

import org.terracotta.masterworker.Worker;

/**
 * Implementation of the AgentOrganizer class
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public class AgentOrganizerImpl implements AgentOrganizer{
	
	/**List of workers involved*/
	@SuppressWarnings("unchecked")
	protected List<Worker> workers;
	
	/**A master object used*/
	@SuppressWarnings("unchecked")
	protected StartMaster startMaster;
	
	/**
	 * Constructor containing the master, workers, and final tick count.
	 * @param startMaster the start manager
	 * @param workers the list of workers
	 * @param lastTick the last tick
	 */
	@SuppressWarnings("unchecked")
	public AgentOrganizerImpl(StartMaster startMaster,List<Worker>workers){
		this.startMaster=startMaster;
		this.workers=workers;
	}
	
	public List<Worker> getWorkers() {
		return workers;
	}

	public void setWorkers(List<Worker> workers) {
		this.workers = workers;
	}

	@SuppressWarnings("unchecked")
	public void stopWorkers(){
		for(Worker worker: workers){
			worker.stop();
		}
	}

	public void manageAgents() {
		// TODO Auto-generated method stub	
	}
}
