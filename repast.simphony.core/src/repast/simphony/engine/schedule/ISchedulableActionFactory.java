package repast.simphony.engine.schedule;

import java.util.List;


/**
 * Interface for Factories for producing ISchedulableAction-s. SchedulableActions are IActions
 * decorated with scheduling data. This factory is used by a Schedule to create ISchedulableAction
 * that the Schedule can then schedule for execution.<p/>
 * 
 * The intention here is that an ISchedulableActionFactory will wrap an IActionFactory and use that
 * IActionFactory to create IActions. The ISchedulableActionFactory will then take those IActions
 * and "decorate" them with scheduling data to create ScheduableActions. In this way, the what (i.e.
 * IAction) of scheduling is separated from the when (i.e. ISchedulableAction) of scheduling.<p/>
 * 
 * The ISchedulableActionFactory must respect the wrapped IAction's
 * {@link repast.simphony.engine.schedule.NonModelAction} annotation by returning the corresponding value
 * from its {@link repast.simphony.engine.schedule.ISchedulableAction#isNonModelAction()} method. For wrapped
 * action with that annotation present, the ISchedulableAction's method should return true, otherwise
 * it should return false.
 * 
 * @see repast.simphony.engine.schedule.Schedule
 * @see repast.simphony.engine.schedule.ISchedulableAction
 * @see repast.simphony.engine.schedule.IAction
 * @see repast.simphony.engine.schedule.IActionFactory
 * 
 * @author Nick Collier
 */
public interface ISchedulableActionFactory  {
  
  /**
   * Creates an ISchedulableAction from the specified scheduling parameters and executable
   * IAction.
   * 
   * @param action the action to execute
   * @param scheduleParams the scheduling data
   * 
   * @return the created ISchedulableAction
   */ 
  ISchedulableAction createAction(ScheduleParameters scheduleParams, IAction action);

  /**
   * Creates a ISchedulableAction from the specified scheduling parameters that will execute
   * the named method on the target object with the specified parameters.
   * 
   * @param scheduleParams the scheduling data
   * @param target the object on which to call the named method
   * @param methodName the name of the method to call
   * @param parameters the parameters to pass to the method
   * 
   * @return the created ISchedulableAction
   */ 
  ISchedulableAction createAction(ScheduleParameters scheduleParams, Object target, String methodName, 
                                        Object... parameters);
  
  
  /**
   * Creates a ISchedulableAction from the specified scheduling parameters that will execute
   * the named method on the objects returned by target object with the specified parameters.
   * 
   * @param scheduleParams the scheduling data
   * @param target the Iterable containing the objects on which to call the named method
   * @param methodName the name of the method to call
   * @param shuffle whether to shuffle the items in the iterable before calling the method on the objects therein
   * @param parameters the parameters to pass to the method
   * 
   * @return the created ISchedulableAction
   */ 
  ISchedulableAction createActionForIterable(ScheduleParameters scheduleParams, Iterable target, String methodName, 
                                        boolean shuffle, Object... parameters);
  
  /**
   * Creates an ISchedulableACtion from ScheduleMethod annotated methods in the specified
   * annotatedObj. The action will execute the method whose parameters best
   * match the specified parameters. The parameters will be passed to the
   * method at execution.
   *
   * @param annotatedObj the object containing the annotated methods
   * @param parameters the parameters to pass to the method call and to use to
   * find the method itself
   * @return the created ISchedulableAction
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   */ 
  ISchedulableAction createAction(Object annotatedObj, Object... parameters);
  
  /**
   * Creates an ISchedulableAction from ScheduleMethod annotated methods in the specified
   * annotatedObj. The action will execute the method whose parameters best
   * match the specified parameters. The parameters will be passed to the method at execution. The
   * method will be executed according to the specified ScheduleParameters. The scheduling parameters
   * derived from the annotation will be ignored. 
   *
   * @param scheduleParams the scheduling parameters describing start time etc.
   * @param annotatedObj the object containing the annotated methods
   * @param parameters the parameters to pass to the method call and to use to
   * find the method itself
   * @return the created ISchedulableAction
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   */
  ISchedulableAction createAction(ScheduleParameters scheduleParams, Object annotatedObj, Object... parameters);

  /**
   * Creates a List of SchedulableActions from annotated methods. The list is created from the methods 
   * in the specified object that are annotated by the ScheduledMethod annotation.
   *  
   * @param obj the object whose methods are annotated
   * @return a List of ScheduableActions created from the annotated methods
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   * @see repast.simphony.engine.schedule.IActionParameterPair
   */ 
  List<ISchedulableAction> createActions(Object obj);

  /**
   * Gets the IActionFactory wrapped by the ISchedulableActionFactory.
   * 
   * @return the IActionFactory wrapped by the ISchedulableActionFactory.
   */ 
  IActionFactory getActionFactory();
}
