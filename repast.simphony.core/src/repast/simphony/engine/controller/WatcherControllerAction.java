package repast.simphony.engine.controller;

import repast.simphony.context.Context;
import repast.simphony.context.ContextListener;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.watcher.WatchAnnotationReader;
import repast.simphony.engine.watcher.WatcherTrigger;
import repast.simphony.parameter.Parameters;

/**
 * Controller action that associates the watcher mechanism with the master
 * context and processes any objects already in the master context. Note that
 * this should only be an action on the master context as all the objects in the
 * sim are in the master context.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class WatcherControllerAction implements ControllerAction {

  private WatchAnnotationReader reader;

  public WatcherControllerAction(WatchAnnotationReader reader) {
    this.reader = reader;
  }

  /**
   * Gets the number of watches.
   * 
   * @return the number of watches
   */
  public int watchCount() {
    return reader.watchCount();
  }

  /**
   * Add a watch context listener to the master context and processes any
   * objects already in the master context. Any other contexts will be ignored.
   * 
   * @param runState
   * @param contextId
   */
  public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
    Context context = runState.getMasterContext();
    ISchedule schedule = runState.getScheduleRegistry().getModelSchedule();
    ContextListener contextListener = reader.getContextListener(schedule);
    boolean doSetup = true;
    for (Object obj : context.getContextListeners()) {
      if (obj.equals(contextListener)) {
        doSetup = false;
        break;
      }
    }

    if (context != null && doSetup) {
      context.addContextListener(contextListener);
      reader.processObjectAsWatcher(context, schedule, context);
      
      for (Class<?> clazz : (Iterable<Class<?>>) context.getAgentTypes()) {
        for (Object obj : context.getObjects(clazz)) {
          reader.processObjectAsWatcher(obj, schedule, context);
        }
      }
    }
  }

  public void batchInitialize(RunState runState, Object contextId) {
  }

  public void runCleanup(RunState runState, Object contextId) {
    WatcherTrigger.getInstance().clearNotifiers();
    reader.reset();
  }

  public void batchCleanup(RunState runState, Object contextId) {
  }
}
