package repast.simphony.visualization;

import java.util.HashMap;
import java.util.Map;

/**
 * A semantic event indicating that diplay event has occured
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DisplayEvent {

  public static final Object NULL_PROPERTY = new Object();
  public static final String TYPE = "__TYPE__";

  private IDisplay source;
  private Object subject;
  private Map<String, Object> properties = new HashMap<String, Object>();

  /**
   * Creates a display event fired from the specified source
   * and with the specified subject.
   *
   * @param source the source of the event
   * @param subject the subject of the event
   */
  public DisplayEvent(IDisplay source, Object subject) {
		this.subject = subject;
		this.source = source;
	}

  /**
   * Gets the subject of the event.
   *
   * @return the subject of the event.
   */
  public Object getSubject() {
    return subject;
  }

  /**
   * Adds a property to this DisplayEvent.
   *
   * @param name the name of the property
   * @param prop the property
   */
  public void addProperty(String name, Object prop) {
    properties.put(name, prop);
  }

  /**
   * Gets the named property
   *
   * @param name the name of the property to get
   * @return the named property or DisplayEvent.NULL_PROPERTY if no such property exists.
   */
  public Object getProperty(String name) {
    Object obj = properties.get(name);
    return obj == null ? NULL_PROPERTY : obj;
  }

  /**
	 * Gets the source of the event.
	 *
	 * @return the source of the event.
	 */
	public IDisplay getSource() {
		return source;
	}
}