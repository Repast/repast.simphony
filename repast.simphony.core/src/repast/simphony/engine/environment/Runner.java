/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.engine.controller.Controller;
import repast.simphony.engine.graph.Executor;

/**
 * Interface for the action that will execute a schedule in the ControllerRegistry.
 * 
 * @author Jerry Vos
 */
public interface Runner extends Executor<RunState> {
	/**
	 * Specifies if the simulation should continue to run (returns true) or if it should stop
	 * (returns false).
	 * 
	 * @return false if the sim should stop, true otherwise
	 */
	boolean go();

	void init();

	void stop();

	// note pause and step don't really apply to all kinds of ScheduleRunner's so we should deal
	// with
	// this at some point.
	void setPause(boolean pause);
	
	void setEnvironmentBuilder(RunEnvironmentBuilder environment);
	
	void step();

	/**
	 * Adds the specified listener to the list of RunListener-s to be notified of any run related
	 * events, such as stopped, started, and so on.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	void addRunListener(RunListener listener);
	
	public void setController(Controller controller);
}
