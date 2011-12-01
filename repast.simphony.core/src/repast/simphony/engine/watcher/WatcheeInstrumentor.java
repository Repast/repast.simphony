package repast.simphony.engine.watcher;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import repast.simphony.engine.watcher.WatcheeDataFinder.FinderResult;
import repast.simphony.filter.AllFilter;
import repast.simphony.filter.Filter;
import simphony.util.messages.MessageCenter;

/**
 * Instruments watchee classes to add the watcher notification mechanism.
 * 
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class WatcheeInstrumentor {

  private static MessageCenter msg = MessageCenter.getMessageCenter(WatcheeInstrumentor.class);

  private WatcheeDataFinder watcheeFinder;

  // set of classnames that have been instrumented over the life of a vm
  private static Set<String> instrumented = new HashSet<String>();

  public WatcheeInstrumentor() {
    watcheeFinder = new WatcheeDataFinder(this);
  }

  /**
   * Adds the named field in the named class as a field to watch.
   * 
   * @param className
   *          the field's class
   * @param fieldName
   *          the field to watch
   */
  public void addFieldToWatch(String className, String fieldName) {
    watcheeFinder.addClassToFind(className, fieldName);
  }

  /**
   * Adds fields to watch from any @Watch annotations on any class that this
   * finds in the specified path. The path can be a top level class directory of
   * classes or a jar file.
   * 
   * @param path
   */
  public void addFieldToWatchFromWatcherPath(String path) {
    watcheeFinder.addPathToSearch(path);
  }

  /**
   * Instrument the fields and classes as Watchees searching the boot classpath
   * as well as the specified classpath.
   * 
   * @param classpath
   */
  public void instrument(String classpath) {
    instrument(classpath, new AllFilter<String>());
  }

  /**
   * Instrument the watched classes. The watched classes are those derived from @Watch
   * annotation found on the added paths set in addFieldtoWatchFromWatcherPaths,
   * and any classes or classes and fields explicitly set in the other add
   * methods. Only classes that pass the filter will be queried for the @Watch
   * annotation.
   * 
   * @param classpath
   *          the classpath to search for the classes to instrument
   * @param filter
   */
  public void instrument(String classpath, Filter<String> filter) {
    try {

      FinderResult result = watcheeFinder.run(classpath, filter);
      if (result.failed()) {
        msg.warn(result.getMessage() + " classpath: " + classpath);
      }

      ClassPool pool = ClassPool.getDefault();
      pool.appendClassPath(new LoaderClassPath(this.getClass().getClassLoader()));

      for (WatcheeData data : watcheeFinder.data()) {
        if (data.path != null && !instrumented.contains(data.className)) {
          BufferedInputStream stream = new BufferedInputStream(data.path.openStream());
          CtClass ctClass = pool.makeClass(stream);
          stream.close();

          for (String field : data.fields) {
            ctClass.instrument(new WatcherEditor(field));
          }
          // this should load the new instrumented class into the class loader
          ctClass.toClass(this.getClass().getClassLoader(), this.getClass().getProtectionDomain());
          instrumented.add(data.className);
          // remove from the watchees, so we won't reinstrument it.
        }
      }
    } catch (IOException e) {
      msg.error("Error while instrumenting watchee class", e);
    } catch (CannotCompileException e) {
      msg.error("Error while instrumenting watchee class", e);
    } catch (ClassNotFoundException e) {
      msg.error("Error while instrumenting watchee class", e);
    }
  }

  /**
   * Gets whether or not the named class has been instrumented as a Watchee.
   * 
   * @param className
   * @return whether or not the named class has been instrumented as a Watchee.
   */
  public boolean isInstrumented(String className) {
    return instrumented.contains(className);
  }

  public static Set<String> getInstrumented() {
    return instrumented;
  }
}
