package repast.simphony.engine.schedule;

import java.io.Serializable;

/**
 * Interface for executable object. Schedules execute objects that 
 * implement this type either directly or by creating an IAction to 
 * perform the appropriate execution.
 * 
 * @see repast.simphony.engine.schedule.Schedule
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public interface IAction  {
  
  /**
   * Executes this IAction, typically by performing a method call on some object.
   */ 
  public void execute();
}
