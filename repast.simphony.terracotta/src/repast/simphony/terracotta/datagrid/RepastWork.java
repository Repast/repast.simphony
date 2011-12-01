package repast.simphony.terracotta.datagrid;

import java.util.List;

import commonj.work.Work;

/**
 * Interface of Repast Simphony objects using CommonJ work.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */
public interface RepastWork extends Work{
	
	/**
	 * Release work item from management.
	 */
	public void release();
	
	/**
	 * Check to see if completed work or not.
	 * @return a boolean check
	 */
	public boolean checkCompleted();

	/**
	 * List of additional work related to another work item.
	 * @return a list of additional repast work
	 */
	public List<RepastWork>returnRelatedWork();
}
