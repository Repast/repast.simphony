package repast.simphony.engine.watcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.context.ContextListener;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.util.collections.Pair;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/10 16:59:23 $
 */
public class WatchAnnotationReader {

  private WatchContextListener contextListener;
  private Map<Class<?>, List<Pair<Method, Watch>>> watchMap = new HashMap<Class<?>, List<Pair<Method, Watch>>>();

  /**
   * Process the specified class for watch annnotations.
   * 
   * @param clazz
   *          the class to process
   */
  public void processClass(Class<?> clazz) {
    findWatchAnnotation(clazz);
  }

  public void processClasses(List<Class<?>> clazzes) {
    // read the spec to find the watch annotations
    for (Class<?> clazz : clazzes) {
      processClass(clazz);
    }
  }

  private void findWatchAnnotation(Class<?> clazz) {
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      // we don't need to use ClassUtilities.deepAnnotationCheck here because
      // class.getMethods() returns all the inherited public methods anyway.
      Watch watch = method.getAnnotation(Watch.class);
      if (watch != null) {
        processWatch(watch, clazz, method);
      } else {
        WatchItems items = method.getAnnotation(WatchItems.class);
        if (items != null) {
          for (Watch aWatch : items.watches()) {
            processWatch(aWatch, clazz, method);
          }
        }
      }
    }
  }
  
  /**
   * Gets the number of watches.
   * 
   * @return the number of watches
   */
  public int watchCount() {
    return watchMap.values().size();
  }

  // processes an individual watch
  private void processWatch(Watch watch, Class<?> clazz, Method method) {
    List<Pair<Method, Watch>> list = watchMap.get(clazz);
    if (list == null) {
      list = new ArrayList<Pair<Method, Watch>>();
      watchMap.put(clazz, list);
    }
    list.add(new Pair<Method, Watch>(method, watch));
  }

  /**
   * Examines the specified object to see if it should be a watcher and if so
   * sets up accordingly.
   * 
   * @param object
   * @param schedule
   * @param context
   */
  public void processObjectAsWatcher(Object object, ISchedule schedule, Context<Object> context) {
    // this call will initialize the contextListener if it hasn't been
    // initialized already
    getContextListener(schedule);
    contextListener.processObject(object, context);
  }

  @SuppressWarnings("rawtypes")
  public ContextListener getContextListener(ISchedule schedule) {
    if (contextListener == null) {
      contextListener = new WatchContextListener(schedule, watchMap);
    }
    return contextListener;
  }

  public void reset() {
    contextListener = null;
  }
}
