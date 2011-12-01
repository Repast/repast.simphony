package repast.simphony.engine.environment;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.controller.Controller;


/**
 * @author Nick Collier
 */
public abstract class AbstractRunner implements Runner {

	protected boolean stop = false;

	protected boolean pause = false;

	protected boolean step = false;

	protected ArrayList<RunListener> runListeners = new ArrayList<RunListener>();
	
	protected Thread thread;
	
	protected RunEnvironmentBuilder environmentBuilder;
	
	protected Controller controller;
	/**
	 * Adds the specified listener to the list of RunListener-s to be notified of any run related
	 * events, such as stopped, started, and so on.
	 * 
	 * @param listener
	 *            the listener to add.
	 */
	public void addRunListener(RunListener listener) {
		synchronized (runListeners) {
			runListeners.add(listener);
		}
	}

	/**
	 * Fires a started message to all registered run listeners.
	 */
	protected void fireStartedMessage() {
		List<RunListener> list = cloneListenerList();
		for (RunListener listener : list) {
			listener.started();
		}
	}

	/**
	 * Fires a stopped message to all registered run listeners.
	 */
	protected void fireStoppedMessage() {
		List<RunListener> list = cloneListenerList();
		for (RunListener listener : list) {
			listener.stopped();
		}
	}
	
	public void setEnvironmentBuilder(RunEnvironmentBuilder environment){
		this.environmentBuilder=environment;
	}

	/**
	 * Fires a paused message to all registered run listeners.
	 */
	protected void firePausedMessage() {
		List<RunListener> list = cloneListenerList();
		for (RunListener listener : list) {
			listener.paused();
		}
	}

	/**
	 * Fires a restarted message to all registered run listeners.
	 */
	protected void fireRestartedMessage() {
		List<RunListener> list = cloneListenerList();
		for (RunListener listener : list) {
			listener.restarted();
		}
	}

	@SuppressWarnings("unchecked")
	private List<RunListener> cloneListenerList() {
		List<RunListener> list = null;
		synchronized (runListeners) {
			list = (List<RunListener>) runListeners.clone();
		}
		return list;
	}

	public void init() {
		stop = false;
		pause = false;
		step = false;
	}

	/**
	 * Stops the execution of the scheduled events after the any events scheduled for the current
	 * tick have been executed.
	 */
	public void stop() {
		stop = true;
	}

	public void step() {
		step = true;
	}

	public void setPause(boolean pause) {
		this.pause = pause;
	}
	

	/**
	 * @return true if the runner should keep going
	 */
	public boolean go() {
		return !stop;
	}
	
	public void setController(Controller controller){
		this.controller=controller;
	}
}
