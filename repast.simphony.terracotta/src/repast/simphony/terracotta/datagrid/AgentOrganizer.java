package repast.simphony.terracotta.datagrid;

/**
 * * Interface to allow project organizer agents and/or other distributed work
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */

public interface AgentOrganizer {

	/**
	 * Method to manage agent actions within the distributed process.
	 */
	public void manageAgents();
	
	/**
	 * Stop workers.
	 */
	public void stopWorkers();
}
