package repast.simphony.engine.schedule;



/**
 * A simple pair object containing an IAction and ScheduleParameters. The
 * ScheduleParameters object contains the scheduling data for its paired
 * IAction.
 * 
 * @author Nick Collier
 * @version $Revision: 1.1 $ $Date: 2005/12/21 22:25:34 $
 */
public class IActionParameterPair  {

	private static final long serialVersionUID = 3256442495255460150L;

	private IAction action;

	private ScheduleParameters params;

	/**
	 * Creates an IActionParameterPair from the specified IAction and
	 * parameters.
	 * 
	 * @param action
	 *            the action
	 * @param params
	 *            the scheduling data for the IAction
	 */
	public IActionParameterPair(IAction action, ScheduleParameters params) {
		this.action = action;
		this.params = params;
	}

	/**
	 * Gets the IAction part of this pair.
	 * 
	 * @return the IAction part of the pair.
	 */
	public IAction getAction() {
		return action;
	}

	/**
	 * Gets the ScheduleParameters part of this pair.
	 * 
	 * @return the ScheduleParameters part of the pair.
	 */
	public ScheduleParameters getParams() {
		return params;
	}
}
