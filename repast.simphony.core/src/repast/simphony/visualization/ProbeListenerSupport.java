package repast.simphony.visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling the broadcast of ProbeEvents.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ProbeListenerSupport {

  private List<ProbeListener> listeners = new ArrayList<ProbeListener>();

  /**
   * Adds a probe listener to the list of listeners to broadcast probe events to.
   *
   * @param listener the listener to add
   */
  public void addProbeListener(ProbeListener listener) {
    synchronized (listeners) {
      listeners.add(listener);
    }
  }
  
  /**
   * Broadcast a probe event consisting of the specified source, probed object 
   * and type to the current list of probe listeners.
   *
   * @param source       the source of the event
   * @param probedObject the probed object
   * @param type the type of the probe
   */
  public void fireProbeEvent(Object source, List<?> probedObject, ProbeEvent.Type type) {
    ProbeEvent evt = new ProbeEvent(source, probedObject, type);
    List<ProbeListener> list;
    synchronized (listeners) {
      list = (List<ProbeListener>) ((ArrayList) listeners).clone();
    }
    for (ProbeListener listener : list) {
      listener.objectProbed(evt);
    }
  }

  /**
   * Broadcast a probe event consisting of the specified source and probed object to the current
   * list of probe listeners.
   *
   * @param source       the source of the event
   * @param probedObject the probed object
   */
  public void fireProbeEvent(Object source, List<?> probedObject) {
    fireProbeEvent(source, probedObject, ProbeEvent.Type.POINT);
  }

  /**
   * Removes all the probeListeners.
   */
  public void removeAllProbeListeners() {
    listeners.clear();
  }


}
