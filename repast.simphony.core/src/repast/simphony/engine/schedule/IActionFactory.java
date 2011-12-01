package repast.simphony.engine.schedule;

import java.util.List;


/**
 * Factory for producing IActions to be executed by a Schedule.
 * 
 * @see repast.simphony.engine.schedule.Schedule
 * @see repast.simphony.engine.schedule.IAction
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface IActionFactory  {
  
  /**
   * Creates an IAction to execute the specified IAction. It is acceptable to return the passed in
   * IAction, if no other wrapping or decoration is necessary. 
   * 
   * @param action the IAction that the created IAction will execute
   * @return the created IAction
   */ 
  IAction createAction(IAction action);

  /**
   * Creates an IAction that will call the named method on the specified target using the
   * specified parameters.
   * 
   * @param target the object on which to call the name method
   * @param methodName the name of the method to call
   * @param parameters the parameters of the method
   * @return an IAction that will call the named method on the specified target using the
   * specified parameters.
   */ 
  IAction createAction(Object target, String methodName, Object... parameters);
  
  
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
  IAction createActionForIterable(Iterable target, String methodName, boolean shuffle, Object... parameters);
  
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
  IActionParameterPair createAction(Object annotatedObj, Object... parameters);

  /**
   * Creates a List of IActionParameterPair-s from annotated methods. The list is created from the methods 
   * in the specified object that are annotated by the ScheduledMethod annotation.
   *  
   * @param obj the object whose methods are annotated
   * @return a List of IAction together with their scheduling parameter data created from 
   * annotated methods.
   * 
   * @see repast.simphony.engine.schedule.ScheduledMethod
   * @see repast.simphony.engine.schedule.IActionParameterPair
   */ 
  List<IActionParameterPair> createActions(Object obj);
}
