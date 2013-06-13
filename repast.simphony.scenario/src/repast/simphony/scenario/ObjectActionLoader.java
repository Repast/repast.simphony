/*CopyrightHere*/
package repast.simphony.scenario;

import java.io.File;
import java.io.Reader;

import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.ControllerRegistry;
import simphony.util.messages.MessageCenter;

import com.thoughtworks.xstream.XStream;

public abstract class ObjectActionLoader<T> extends AbstractActionLoader {
  private static final MessageCenter LOG = MessageCenter.getMessageCenter(ObjectActionLoader.class);

  protected Class<T> dataClass;
  protected String actionRoot;

  public ObjectActionLoader(File file, Object contextID, Class<T> dataClass, String actionRoot) {
    super(file, contextID);
    this.dataClass = dataClass;
    this.actionRoot = actionRoot;
  }

  protected void performLoad(Reader reader, XStream xstream, Scenario scenario,
      ControllerRegistry registry) {
    ClassLoader loader = xstream.getClassLoader();
    try {
      xstream.setClassLoader(getClassLoader());
      prepare(xstream);

      T descriptor;
      descriptor = dataClass.cast(xstream.fromXML(reader));
      ControllerAction action = createAction(descriptor, scenario);
      ControllerAction parent = registry.findAction(contextID, actionRoot);
      registry.addAction(contextID, parent, action);
    } catch (RuntimeException ex) {
      LOG.warn("Error loading information from data. Continuing with model loading.", ex);
    } finally {
      if (loader != null) {
        xstream.setClassLoader(loader);
      }
    }
  }

  /**
   * Override this method if you need to prepare the XStream before using it.
   * 
   * @param xstream
   *          the XStream that will be used for reading in the descriptor
   */
  protected void prepare(XStream xstream) {
    // do nothing
  }

  protected abstract ControllerAction createAction(T data, Scenario scenario);

  protected abstract ClassLoader getClassLoader();
}