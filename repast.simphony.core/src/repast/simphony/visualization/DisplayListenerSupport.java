package repast.simphony.visualization;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for handling the broadcast of display events.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DisplayListenerSupport {

	private List<DisplayListener> listeners = new ArrayList<DisplayListener>();

	/**
	 * Adds a display listener to the list of listeners to broadcast
   * display events to.
	 *
	 * @param listener the listener to add
	 */
	public void addDisplayListener(DisplayListener listener) {
		synchronized (listeners) {
			listeners.add(listener);
		}
	}



  /**
	 * Broadcast a info message display event consisting of the specified source and
   * the specified message.
	 *
	 * @param source the source of the event
	 * @param message the message to broadcast
	 */
	public void fireInfoMessage(IDisplay source, String message) {
    DisplayEvent evt = new DisplayEvent(source, message);
    fireInfoMessage(evt);
  }

  /**
	 * Broadcast a info message display event.
   *
	 * @param evt the display event to broacast
	 */
  public void fireInfoMessage(DisplayEvent evt) {
    List<DisplayListener> list;
		synchronized (listeners) {
			list = (List<DisplayListener>)((ArrayList)listeners).clone();
		}
		for (DisplayListener listener : list) {
			listener.receiveInfoMessage(evt);
		}
	}
}