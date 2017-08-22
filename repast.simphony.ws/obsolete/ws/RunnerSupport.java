package repast.simphony.ws;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.engine.environment.RunListener;

/**
 * Manages RunListeners.
 * 
 * @author Nick Collier
 */
public class RunnerSupport {

  protected ArrayList<RunListener> runListeners = new ArrayList<RunListener>();

  /**
   * Adds the specified listener to the list of RunListener-s to be notified of
   * any run related events, such as stopped, started, and so on.
   * 
   * @param listener
   *          the listener to add.
   */
  public void addRunListener(RunListener listener) {
    synchronized (runListeners) {
      runListeners.add(listener);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.engine.environment.Runner#removeRunListener(repast.simphony
   * .engine.environment.RunListener)
   */

  public void removeRunListener(RunListener listener) {
    synchronized (runListeners) {
      runListeners.remove(listener);
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
}
