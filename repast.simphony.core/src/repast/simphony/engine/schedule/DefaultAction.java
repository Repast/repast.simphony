package repast.simphony.engine.schedule;


/**
 * Default SchedulableAction used by as a default by the scheduling mechanism. A DefaultAction
 * essentially combines an IAction to execute with the data necessary to schedule
 * that IAction for execution.
 * 
 * @see repast.simphony.engine.schedule.IAction
 * @see repast.simphony.engine.schedule.ISchedulableAction
 *   
 * @author Nick Collier
 */
public class DefaultAction extends AbstractAction {
  
  static final long serialVersionUID = 3628821571113796716L;
  
  private IAction actionToExecute;

  /**
   * Creates a DefaultAction to execute the specified action according to the specified scheduling parameters.
   *  
   * @param params the scheduling parameters for this DefaultAction
   * @param actionToExecute the IAction to execute when this DefaultAction is executed
   * @param orderIndex the order in which this was added to a schedule
   */ 
  public DefaultAction(ScheduleParameters params, IAction actionToExecute, long orderIndex) {
    super(params, orderIndex);
    this.actionToExecute = actionToExecute;

    setIsNonModelAction(actionToExecute);
  }
  
  /**
   * Executes the IAction specified in the constructor.
   */ 
  public void execute() {
    actionToExecute.execute();
  }
}
