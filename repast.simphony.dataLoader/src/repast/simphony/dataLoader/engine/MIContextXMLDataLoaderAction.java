package repast.simphony.dataLoader.engine;

import java.io.IOException;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import repast.simphony.scenario.data.Classpath;
import repast.simphony.scenario.data.ContextData;
import repast.simphony.scenario.data.UserPathData;
import repast.simphony.util.ClassPathEntry;
import simphony.util.messages.MessageCenter;

/**
 * DataLoaderAction that uses a {@link MIContextXMLBuilder} to build the
 * context.
 * 
 * @author Nick Collier
 */
public class MIContextXMLDataLoaderAction extends DataLoaderControllerAction<MIContextXMLBuilder> {

  private static MessageCenter msg = MessageCenter
      .getMessageCenter(MIContextXMLDataLoaderAction.class);

  /**
   * Creates this {@link MIContextXMLDataLoaderAction} using the specified
   * context and path data
   * 
   * @param root
   *          the root ContextData
   * @param pathData
   *          the user path data for the scenario
   */
  public MIContextXMLDataLoaderAction(ContextData root, UserPathData pathData) {
    super("XML & Model Init Context Builder", null, root);
    Class<?> initClass = null;
    FastMethod initializer = null;
    try {
      for (ClassPathEntry entry : pathData.classpathEntries()) {
        Classpath path = new Classpath();
        path.addEntry(entry);
        initClass = findModelInit(path);
        if (initClass != null) {
          FastClass fclass = FastClass.create(this.getClass().getClassLoader(), initClass);
          initializer = fclass.getMethod("initializeModel", new Class<?>[0]);
          break;
        }
      }
    } catch (Exception ex) {
      msg.error("ModelInitializer class was not found. Unable to initialize scenario", ex);
    }

    if (initClass != null) {
      setBuilder(new MIContextXMLBuilder(root, initClass, initializer));
    } else {
      msg.error("ModelInitializer class was not found. Unable to initialize scenario", null);
    }
  }

  private Class<?> findModelInit(Classpath path) throws SecurityException, IOException,
      ClassNotFoundException {

    for (Class<?> clazz : path.getClasses()) {
      if (clazz.getSimpleName().equals("ModelInitializer")) {
        try {
          clazz.getMethod("initializeModel");
          return clazz;
        } catch (NoSuchMethodException ex) {
        }
      }
    }
    return null;
  }
}
