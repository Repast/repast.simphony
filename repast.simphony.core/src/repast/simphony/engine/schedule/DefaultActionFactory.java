package repast.simphony.engine.schedule;

import repast.simphony.util.ClassUtilities;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Default factory for producing IActions to be executed by a Schedule.
 *
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 * @see repast.simphony.engine.schedule.Schedule
 * @see repast.simphony.engine.schedule.IAction
 */
public class DefaultActionFactory implements IActionFactory {

  static final long serialVersionUID = 1146802409368283440L;

  /**
   * Creates an IAction to execute the specified IAction. This default
   * implementation simply returns the parameter.
   *
   * @param action the IAction that the created IAction will execute
   * @return the created IAction
   */
  public IAction createAction(IAction action) {
    return action;
  }

  /**
   * Creates an IAction that will call the named method on the specified target using the
   * specified parameters.
   *
   * @param target     the object on which to call the name method
   * @param methodName the name of the method to call
   * @param parameters the parameters of the method
   * @return an IAction that will call the named method on the specified target using the
   *         specified parameters.
   */
  public IAction createAction(Object target, String methodName, Object... parameters) {
    return new CallBackAction(target, methodName, parameters);
  }
/**
   * Creates an IAction that will call the named method on each object in the specified target using the
   * specified parameters.
   * 
   * @param target the object on which to call the name method
   * @param methodName the name of the method to call
   * @param shuffle whether to shuffle the items in the iterable before calling the method on the objects therein
   * @param parameters the parameters of the method
   * @return an IAction that will call the named method on the specified target using the
   * specified parameters.
   */ 
  public IAction createActionForIterable(Iterable target, String methodName, boolean shuffle, Object... parameters) {
    return new IterableCallBackAction(target, methodName, shuffle, parameters);
  }

  /**
   * Creates an IActionParameterPair from annotated methods in the specified
   * annotatedObj. The IAction will execute the method whose parameters best 
   * match the specified parameters. The parameters will be passed to the
   * method at execution.
   *
   * @param annotatedObj the object containing the annotated methods
   * @param parameters the parameters to pass to the method call and to use to
   * find the method itself
   * @return an IActionParameterPair containing the best matching IAction and its
   * ScheduleParameters.
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   * @see repast.simphony.engine.schedule.IActionParameterPair
   */ 
  public IActionParameterPair createAction(Object annotatedObj, Object... parameters) {
    Class clazz = annotatedObj.getClass();
    Map<String, ScheduleParameters> nameParamMap = findCandidateMethodMatches(clazz);

    Class[] paramTypes = new Class[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      paramTypes[i] = parameters[i].getClass();
    }

    List<Method> matches = new ArrayList<Method>();
    for (String name : nameParamMap.keySet()) {
      Method method = ClassUtilities.findMethod(clazz, name, paramTypes);
      if (method != null) {
        matches.add(method);
      }
    }

    if (matches.size() != 1) {
      String pTypes = "(";
      for (int i = 0; i < paramTypes.length; i++) {
        if (i > 0) pTypes += ", ";
        pTypes += paramTypes[i].getName();
      }
      pTypes += ")";
      if (matches.size() == 0)
        throw new IllegalArgumentException("Unable to find match for ScheduleMethod annotated object '"
                + clazz.getName() + "' with method parameters " + pTypes);
      else
        throw new IllegalArgumentException("Too many matches for ScheduleMethod annotated object '"
                + clazz.getName() + "' with method parameters " + pTypes);
    }


    Method method = matches.get(0);
    CallBackAction action = new CallBackAction(annotatedObj, method, parameters);
    return new IActionParameterPair(action, nameParamMap.get(method.getName()));
  }

  private Map<String, ScheduleParameters> findCandidateMethodMatches(Class clazz) {
    Method[] methods = clazz.getMethods();
    // will contain the list of candidate method names
    Map<String, ScheduleParameters> nameParamMap = new HashMap<String, ScheduleParameters>();
    for (Method method : methods) {
      ScheduledMethod scheduledMethod = ClassUtilities.deepAnnotationCheck(method, ScheduledMethod.class);
      if (scheduledMethod != null) {
        String methodName = method.getName();
        ScheduleParameters params;
        double duration = scheduledMethod.duration();
        if (scheduledMethod.interval() > 0) {
          params = ScheduleParameters.createRepeating(scheduledMethod.start(),
                  scheduledMethod.interval(), scheduledMethod.priority(), duration);
        } else {
          params = ScheduleParameters.createOneTime(scheduledMethod.start(), scheduledMethod.priority(),
                  duration);
        }
        nameParamMap.put(methodName, params);
      }
    }
    return nameParamMap;
  }

  /**
   * Creates a List of IActionParameterPair-s from annotated methods. The list is created from the methods
   * in the specified object that are annotated by the ScheduledMethod annotation.
   *
   * @param obj the object whose methods are annotated
   * @return a List of IAction together with their scheduling parameter data created from
   *         annotated methods.
   * @see repast.simphony.engine.schedule.ScheduledMethod
   * @see repast.simphony.engine.schedule.IActionParameterPair
   */
  public List<IActionParameterPair> createActions(Object obj) {
    Class clazz = obj.getClass();
    Method[] methods = clazz.getMethods();
    List<IActionParameterPair> actions = new ArrayList<IActionParameterPair>();
    for (Method method : methods) {
      ScheduledMethod scheduledMethod = method.getAnnotation(ScheduledMethod.class);
      if (scheduledMethod != null && method.getParameterTypes().length == 0 && scheduledMethod.start() != 
              ScheduledMethod.NO_PARAMETERS) 
      {
        CallBackAction action = new CallBackAction(obj, method);
        ScheduleParameters params;
        double duration = scheduledMethod.duration();
        if (scheduledMethod.start() == ScheduledMethod.END) {
          params = ScheduleParameters.createAtEnd(scheduledMethod.priority());
        } else if (scheduledMethod.interval() > 0) {
          params = ScheduleParameters.createRepeating(scheduledMethod.start(),
                  scheduledMethod.interval(), scheduledMethod.priority(), duration);
        } else {
          params = ScheduleParameters.createOneTime(scheduledMethod.start(), scheduledMethod.priority(),
                  duration);
        }
        actions.add(new IActionParameterPair(action, params));
      }
    }
    return actions;
  }
}
