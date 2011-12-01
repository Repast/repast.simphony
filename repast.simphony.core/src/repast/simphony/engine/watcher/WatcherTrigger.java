package repast.simphony.engine.watcher;

import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Triggers registered watchers when a watched field and so on in a watchee fires.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
// This is unfortunately a singleton because we need to have access to it via the instrumented field access
// in the watchers.
public class WatcherTrigger {

  private static MessageCenter msg = MessageCenter.getMessageCenter(WatcherTrigger.class);

  private static WatcherTrigger instance;

  // first key ids the watchee class, field, the inner key ids the @Watch annotation data
  private Map<String, Map<NotifierID, Notifier2>> notifierMap = new HashMap<String, Map<NotifierID, Notifier2>>();
  // watch and list of ids of the notifiers of that watcher
  private Map<Object, List<Notifier2>> objNotifierMap = new HashMap<Object, List<Notifier2>>();
  private WatcheeInstrumentor instrumentor;

  /**
   * Gets the instance of the WatcherTrigger. initInstance must have been
   * called prior to this or this will return null.
   *
   * @return the instance of the WatcherTrigger.
   */
  public static WatcherTrigger getInstance() {
    return instance;
  }

  /**
   * Initializes the WatcherTrigger instance with the specified instrumentor.
   *
   * @param instrumentor
   */
  public static void initInstance(WatcheeInstrumentor instrumentor) {
    instance = new WatcherTrigger(instrumentor);
  }

  private WatcherTrigger(WatcheeInstrumentor instrumentor) {
    this.instrumentor = instrumentor;
  }

  /**
   * Adds a watch of a field set.
   *
   * @param params
   * @param schedule
   */
  public void addFieldSetWatch(WatchParameters params, ISchedule schedule) {
    String id = params.createWatcheeID();
    if (!instrumentor.isInstrumented(params.getClassName())) {
      msg.warn("Class '" + params.getClassName() + "' has not been prepared for watching", new RuntimeException());
      return;
    }

    Map<NotifierID, Notifier2> map = notifierMap.get(id);
    if (map == null) {
      map = new HashMap<NotifierID, Notifier2>();
      notifierMap.put(id, map);
    }

    NotifierID watchID = new NotifierID(params);
    Notifier2 notifier = map.get(watchID);
    if (notifier == null) {
      notifier = new Notifier2(watchID, params, schedule);
      map.put(watchID, notifier);
    } else {
      notifier.addWatcher(params.getWatcher());
    }

    Object watcher = params.getWatcher();
    List<Notifier2> notList = objNotifierMap.get(watcher);
    if (notList == null) {
      notList = new ArrayList<Notifier2>();
      objNotifierMap.put(watcher, notList);
    }
    notList.add(notifier);
  }

  /**
   * Removes all the Notifiers associated with the specified watcher.
   *
   * @param watcher the watcher whose Notifiers we want to remove
   */
  public void removeNotifier(Object watcher) {
    List<Notifier2> notifiers = objNotifierMap.remove(watcher);
    // maybe null if the "watcher" is removed from the context
    // but was never set up as a watcher
    if (notifiers != null) {
      for (Notifier2 notifier : notifiers) {
        notifier.removeWatcher(watcher);
      }
    }
  }

  // id is the instrumented id. The id is tied to
  // the class where the watched field is declared. So,
  // Any change to the field by any class in the heirarchy
  // will trigger with this id. 
  private void notify(String id, Object watchee, Object val) {
    if (notifierMap.size() > 0) {
      Map<NotifierID, Notifier2> notifiers = notifierMap.get(id);
      if (notifiers != null) {
        for (Notifier2 notifier : notifiers.values()) {
          notifier.triggered(watchee, val);
        }
      }
    }
  }

  // we need to all these different methods because
  // the call to these methods is dynamically created and that
  // will not do the autoboxing.
  public void triggered(String id, Object watchee, Object val) {
    notify(id, watchee, val);
  }

  public void triggered(String id, Object watchee, double val) {
    notify(id, watchee, val);
  }

  public void triggered(String id, Object watchee, int val) {
    notify(id, watchee, val);
  }

  public void triggered(String id, Object watchee, float val) {
    notify(id, watchee, val);
  }

  public void triggered(String id, Object watchee, long val) {
    notify(id, watchee, val);
  }

  public void triggered(String id, Object watchee, boolean val) {
    notify(id, watchee, val);
  }

  public void clearNotifiers() {
    notifierMap.clear();
    objNotifierMap.clear();
  }
}
