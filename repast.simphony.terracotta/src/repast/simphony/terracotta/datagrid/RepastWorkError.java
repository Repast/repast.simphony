package repast.simphony.terracotta.datagrid;

import org.terracotta.workmanager.statik.StaticWorkManager;

import commonj.work.Work;

/**
 * Class to throw work error in distribution run.
 * @author Mark Altaweel
 * @version $Revision: 1.2
 */

public class RepastWorkError extends Throwable implements Work {

	private static final long serialVersionUID = 1L;
	
	/**The throwable error*/
	private Throwable t;

	/**
	 * Constructor taking a StaticWorkManger that schedules the failure.
	 * @param failure a StaticWorkManager failure manager
	 */
	public RepastWorkError(StaticWorkManager failure){
		failure.schedule(this);
	}
	
	/**
	 * Constructor for associating the error with a work manager.
	 * @param failure the failure manager
	 * @param t a throwable object
	 */
	public RepastWorkError(StaticWorkManager failure, Throwable t){
		this.t=t;
		failure.schedule(this);
	}

	public boolean isDaemon() {
		return false;
	}

	public void release() {
		
	}

	public void run(){
		if(t!=null){
			run(t);
		}
		else
			new Throwable("Unable to handle work item status, work not processed");
	}
	
	/**
	 * Run method using throwable.
	 * @param t the throwable error
	 */
	public void run(Throwable t) {
		
		new Throwable("Unable to handle work item status, work not processed",t);
	}
}
