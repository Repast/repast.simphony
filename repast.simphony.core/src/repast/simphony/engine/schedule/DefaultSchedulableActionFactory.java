package repast.simphony.engine.schedule;

import java.util.ArrayList;
import java.util.List;


/**
 * Default implementation of an ISchedulableActionFactory. This creates IActions using
 * a IActionFactory and then uses those IActions in conjunction with ScheduleParameters
 * to create ScheduableActions.
 * 
 * @see repast.simphony.engine.schedule.ISchedulableAction
 * @see repast.simphony.engine.schedule.IAction
 * @see repast.simphony.engine.schedule.IActionFactory
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class DefaultSchedulableActionFactory implements ISchedulableActionFactory {
  
  static final long serialVersionUID = 3797781891538451949L;
  
  /**
   * The factory used to create IActions.
   */ 
  protected IActionFactory actionFactory;
  
  /**
   * Tracks the order index for adding actions.
   */ 
  protected long currentOrderIndex = 0;
  
  /**
   * Creates a DefaultSchedulableActionFactory that uses a DefaultActionFactory 
   * to create IActions.
   */ 
  public DefaultSchedulableActionFactory() {
    this(new DefaultActionFactory());
  }
  
  /**
   * Creates a DefaultSchedulableActionFactory that uses the specified action factory
   * to create IActions.
   */
  public DefaultSchedulableActionFactory(IActionFactory actionFactory) {
    this.actionFactory = actionFactory;
  }
  
  /**
   * Creates an ISchedulableAction from the specified scheduling parameters and executable
   * IAction.
   * 
   * @param action the action to execute
   * @param scheduleParams the scheduling data
   * 
   * @return the created ISchedulableAction
   */ 
  public ISchedulableAction createAction(ScheduleParameters scheduleParams, IAction action) {
    IAction anAction = actionFactory.createAction(action);
    if (scheduleParams.getDuration() > 0) {
      return new ThreadedAction(scheduleParams, action, currentOrderIndex++);
    }
    
    return new DefaultAction(scheduleParams, anAction, currentOrderIndex++);
  }
  
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
  public ISchedulableAction createAction(ScheduleParameters scheduleParams, Object target, String methodName, 
                                        Object... parameters) 
  {
    IAction action = actionFactory.createAction(target, methodName, parameters);
    
    if (scheduleParams.getDuration() > 0) {
      return new ThreadedAction(scheduleParams, action, currentOrderIndex++);
    }
    return new DefaultAction(scheduleParams, action, currentOrderIndex++);
  }

  /**
   * Creates a ISchedulableAction from the specified scheduling parameters that will execute
   * the named method on the objects returned by target object with the specified parameters.
   *
   * @param scheduleParams the scheduling data
   * @param target         the Iterable containing the objects on which to call the named method
   * @param methodName     the name of the method to call
   * @param shuffle whether to shuffle the items in the iterable before calling the method on the objects therein
   * @param parameters     the parameters to pass to the method
   * @return the created ISchedulableAction
   */
  public ISchedulableAction createActionForIterable(ScheduleParameters scheduleParams, Iterable target, 
                                                    String methodName, boolean shuffle, Object... parameters) {
    IAction action = actionFactory.createActionForIterable(target, methodName, shuffle, parameters);
    
    if (scheduleParams.getDuration() > 0) {
      return new ThreadedAction(scheduleParams, action, currentOrderIndex++);
    }
    return new DefaultAction(scheduleParams, action, currentOrderIndex++); 
  }

  /**
   * Creates an ISchedulableACtion from ScheduleMethod annotated methods in the specified
   * annotatedObj. The action will execute the method whose parameters best
   * match the specified parameters. The parameters will be passed to the
   * method at execution.
   *
   * @param annotatedObj the object containing the annotated methods
   * @param parameters   the parameters to pass to the method call and to use to
   *                     find the method itself
   * @return the created ISchedulableAction
   * @see ScheduledMethod
   */
  public ISchedulableAction createAction(Object annotatedObj, Object... parameters) {
    IActionParameterPair pair = actionFactory.createAction(annotatedObj, parameters);
    ScheduleParameters params = pair.getParams();
    if (params.getDuration() > 0) return new ThreadedAction(params, pair.getAction(), currentOrderIndex++);
    return new DefaultAction(params, pair.getAction(), currentOrderIndex++);
  }

  /**
   * Creates an ISchedulableAction from ScheduleMethod annotated methods in the specified
   * annotatedObj. The action will execute the method whose parameters best
   * match the specified parameters. The parameters will be passed to the method at execution. The
   * method will be executed according to the specified ScheduleParameters. The scheduling parameters
   * derived from the annotation will be ignored.
   *
   * @param scheduleParams the scheduling parameters describing start time etc.
   * @param annotatedObj   the object containing the annotated methods
   * @param parameters     the parameters to pass to the method call and to use to
   *                       find the method itself
   * @return the created ISchedulableAction
   * @see ScheduledMethod
   */
  public ISchedulableAction createAction(ScheduleParameters scheduleParams, Object annotatedObj, Object... parameters) {
    IActionParameterPair pair = actionFactory.createAction(annotatedObj, parameters);
    if (scheduleParams.getDuration() > 0) {
      return new ThreadedAction(scheduleParams, pair.getAction(), currentOrderIndex++);
    }
    return new DefaultAction(scheduleParams, pair.getAction(), currentOrderIndex++);
  }

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
  public List<ISchedulableAction> createActions(Object obj) {
    List<ISchedulableAction> actions = new ArrayList<ISchedulableAction>();
    List<IActionParameterPair> pairs = actionFactory.createActions(obj);
    for (IActionParameterPair pair: pairs) {
      ScheduleParameters params = pair.getParams();
      if (params.getDuration() > 0)
        actions.add(new ThreadedAction(params, pair.getAction(), currentOrderIndex++));
      else
        actions.add(new DefaultAction(params, pair.getAction(), currentOrderIndex++));
    }
    
    return actions;
  }
  
  /**
   * Gets the IActionFactory used to create the IActions needed by this DefaultSchedulableActionFactory. 
   * 
   * @return the IActionFactory used to create the IActions needed by this DefaultSchedulableActionFactory. 
   */ 
  public IActionFactory getActionFactory() {
    return actionFactory;
  }
}
