package repast.simphony.engine.controller;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.context.ContextEvent;
import repast.simphony.context.ContextListener;
import repast.simphony.engine.environment.ControllerAction;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.DynamicTargetAction;
import repast.simphony.engine.schedule.IAction;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.ScheduleParameters;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;

/**
 * @author Nick Collier
 * @version $Revision: 1.2 $ $Date: 2006/01/10 16:59:05 $
 */
public class ScheduledMethodControllerAction implements ControllerAction, ContextListener{

  static class PickData {
    Object target;
    Method method;
    ScheduledMethod annotation;
    long pickValue;
    long pickCount = 0;
    boolean shuffle = true;

    public PickData(Method method, ScheduledMethod annotation) {
      this.method = method;
      this.annotation = annotation;
      this.shuffle = this.annotation.shuffle();
    }

    public ScheduleParameters getParameters() {
      double duration = annotation.duration();
      ScheduleParameters params;
      if (annotation.start() == ScheduledMethod.END) {
        params = ScheduleParameters.createAtEnd(annotation.priority());
      } else if (annotation.interval() > 0) {
        params = ScheduleParameters.createRepeating(annotation.start(), annotation.interval(),
            annotation.priority(), duration);
      } else {
        params = ScheduleParameters.createOneTime(annotation.start(), annotation.priority(),
            duration);
      }
      return params;
    }
  }

  private List<PickData> pickData = new ArrayList<PickData>();
  private Set<Class> processedClasses = new HashSet();
  private Set<Method> processedMethods = new HashSet();

  private ISchedule schedule;
  // we have to track that actions added for contexts and subcontexts
  // so if those are removed then we remove their actions
  private Map<Object, ISchedulableAction> contextActions = new HashMap<Object, ISchedulableAction>();

  /**
   * Creates a ScheduledMethodControllerAction with no methods scheduled.
   */
  public ScheduledMethodControllerAction() {
  }

  /**
   * Creates a ScheduledMethodControllerAction based on the agent classes
   * contained in the specified data.
   * 
   * @param clazzes
   */
  public ScheduledMethodControllerAction(List<Class<?>> clazzes) {
    // read the spec to find the schedule annotations and get the "pick"
    // information
    processAnnotations(clazzes);
  }

  /**
   * Process any ScheduledMethod annotations on the specified class.
   * 
   * @param clazzes
   */
  public void processAnnotations(List<Class<?>> clazzes) {
    for (Class clazz : clazzes) {
      processAnnotations(clazz);
    }
    
    for (Class clazz : clazzes) {
      Set<Class<?>> interfaces = new HashSet<Class<?>>();
      gatherInterfaces(clazz, interfaces);
      for (Class<?> inter : interfaces) {
        if (!processedClasses.contains(inter)) {
          processAnnotations(inter);
        }
      }
    }
  }
  
  private void gatherInterfaces(Class<?> clazz, Set<Class<?>> interfaces) {
    if (clazz.equals(Object.class)) return;
    Class<?>[] inters = clazz.getInterfaces();
    for (Class<?> inter : inters) {
      interfaces.add(inter);
      gatherInterfaces(inter, interfaces);
    }
    
    if (clazz.getSuperclass() != null) gatherInterfaces(clazz.getSuperclass(), interfaces);
  }

  public void processAnnotations(Class<?> clazz) {
    // if its a context, don't process from the class
    // process during run intialize
    if (!Context.class.isAssignableFrom(clazz)) {
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        // If the method has already been processed, skip it. This prevents
        // a subclass from causing duplicate entries of the superclass methods.
        if (!processedMethods.contains(method))
          processMethod(method);
      }
      processedClasses.add(clazz);
    }
  }

  private PickData processMethod(Method method) {
    // we don't need to use ClassUtilities.deepAnnotationCheck here because
    // class.getMethods() returns all the inherited public methods anyway.
    ScheduledMethod schedule = method.getAnnotation(ScheduledMethod.class);
    if (schedule != null) {
      PickData data = new PickData(method, schedule);
      pickData.add(data);
      processedMethods.add(method);
      return data;
    }

    return null;
  }

  public void batchInitialize(RunState runState, Object contextId) {
  }

  // process the context for any annotations
  private void processContext(Context context) {
    Class<? extends Context> clazz = context.getClass();
    if (!processedClasses.contains(clazz)) {
      boolean found = false;
      Method[] methods = clazz.getMethods();
      for (Method method : methods) {
        PickData data = processMethod(method);
        if (data != null) {
          data.target = context;
          found = true;
        }
      }
      if (found) {
        context.addContextListener(this);
      }
      processedClasses.add(clazz);
    }

    // process the subcontexts
    for (Context child : (Iterable<Context>) context.getSubContexts()) {
      processContext(child);
    }
  }

  public void runInitialize(RunState runState, Object contextId, Parameters runParams) {
    Context context = runState.getMasterContext();
    processContext(context);
    schedule = runState.getScheduleRegistry().getModelSchedule();
    scheduleMethods(context, schedule);
  }

  private void scheduleMethods(Context context, ISchedule schedule) {
    for (PickData data : pickData) {
      if (Modifier.isStatic(data.method.getModifiers())) {
        StaticMethodAction action = new StaticMethodAction(new DynamicTargetAction(data.method));
        schedule.schedule(data.getParameters(), action);
      } else if (data.target != null) {
        DynamicTargetAction action = new DynamicTargetAction(data.method);
        action.setTarget(data.target);
        ISchedulableAction schAction = schedule.schedule(data.getParameters(), action);
        contextActions.put(data.target, schAction);
      } else if (data.pickValue == ScheduledMethod.ALL) {
        IAction action = new ScheduleMethodAllAction(context, data.method.getDeclaringClass(),
            data.method, data.shuffle);
        schedule.schedule(data.getParameters(), action);
      } else {
        IAction action = new ScheduleMethodAction(context, data.method.getDeclaringClass(),
            data.method, data.annotation.pick(), data.shuffle);
        schedule.schedule(data.getParameters(), action);
      }
    }
  }

  private static class StaticMethodAction implements IAction {

    private DynamicTargetAction action;

    private StaticMethodAction(DynamicTargetAction action) {
      this.action = action;
    }

    public void execute() {
      action.execute();
    }
  }

  /**
   * IAction for executing the methods annotated with the ScheduledMethod
   * annotation.
   */
  private static class ScheduleMethodAction implements IAction {

    private Context context;
    private Class<?> targetClass;
    private DynamicTargetAction action;
    private long count;
    boolean shuffle;

    public ScheduleMethodAction(Context context, Class<?> targetClass, Method method, long count) {
      this(context, targetClass, method, count, true);
    }

    public ScheduleMethodAction(Context context, Class<?> targetClass, Method method, long count,
        boolean shuffle) {
      this.context = context;
      this.targetClass = targetClass;
      action = new DynamicTargetAction(method);
      this.count = count;
      this.shuffle = shuffle;
    }

    /**
     * Executes this IAction, retrieving the objects from the context and then
     * executing the methon on each one.
     */
    public void execute() {
      if (this.shuffle) {
        for (Object obj : context.getRandomObjects(targetClass, count)) {
          action.setTarget(obj);
          action.execute();
        }
      } else {
        for (Object obj : context.getObjects(targetClass)) {
          action.setTarget(obj);
          action.execute();
        }
      }
    }
  }

  /**
   * IAction for executing the methods annotated with the ScheduledMethod
   * annotation. The method will be executed on all the objects in the context.
   */
  private static class ScheduleMethodAllAction implements IAction {

    private Context context;
    private Class<?> targetClass;
    private DynamicTargetAction action;
    private boolean shuffle;

    public ScheduleMethodAllAction(Context context, Class<?> targetClass, Method method) {
      this(context, targetClass, method, true);
    }

    public ScheduleMethodAllAction(Context context, Class<?> targetClass, Method method,
        boolean shuffle) {
      this.context = context;
      this.targetClass = targetClass;
      action = new DynamicTargetAction(method);
      this.shuffle = shuffle;
    }

    /**
     * Executes this IAction, retrieving the objects from the context and then
     * executing the method on each one.
     */
    public void execute() {
      Iterable iter;
      if (this.shuffle) {
        iter = context.getRandomObjects(targetClass, context.size());
      } else {
        iter = context.getObjects(targetClass);
      }
      for (Object obj : iter) {
        action.setTarget(obj);
        action.execute();
      }
    }
  }

  public void runCleanup(RunState runState, Object contextId) {
    processedClasses.clear();
    processedMethods.clear();
    contextActions.clear();
    // remove any pickData that contains context as these will be
    // reprocessed during runInit
    for (Iterator<PickData> iter = pickData.iterator(); iter.hasNext();) {
      PickData data = iter.next();
      if (data.target != null && data.target instanceof Context) {
        iter.remove();
      }
    }
  }

  public void batchCleanup(RunState runState, Object contextId) {
    // To change body of implemented methods use File | Settings | File
    // Templates.
  }

  /**
   * Notify this event of a change to a context.
   * 
   * @param ev
   *          The event of which to notify the listener.
   */
  public void eventOccured(ContextEvent ev) {
    ContextEvent.EventType type = ev.getType();
    if (type == ContextEvent.EventType.SUBCONTEXT_ADDED) {
      processContext(ev.getSubContext());
    } else if (type == ContextEvent.EventType.SUBCONTEXT_REMOVED) {
      ISchedulableAction action = contextActions.get(ev.getSubContext());
      schedule.removeAction(action);
    }
  }
}
