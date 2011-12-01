/*CopyrightHere*/
package repast.simphony.engine.environment;

import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

/**
 * This class executes the model schedule found in the RunState's ScheduleRegistry.
 * 
 * @author Jerry Vos
 */
public class DefaultScheduleRunner extends AbstractRunner {
	private static final MessageCenter msgCenter = MessageCenter
			.getMessageCenter(DefaultScheduleRunner.class);
	
	
  /**
	 * This executes the given RunState object's schedule. It will continue executing the schedule
	 * until there are no more actions schedule or the run manager tells it to stop.
	 * 
	 * @param toExecuteOn
	 *            the RunState to execute on
	 */
	public void execute(RunState toExecuteOn) {
		ISchedule schedule = getSchedule(toExecuteOn);
		try {
			while (go() && schedule.getActionCount() > 0) {
				if (schedule.getModelActionCount() == 0) {
					schedule.setFinishing(true);
				}
				schedule.execute();
				// System.out.println("-----" + schedule.getTickCount() +
				// "-----");
				// try {
				// Thread.sleep(200);
				// } catch (InterruptedException e) {
				// e.printStackTrace();
				// }
			}
			schedule.executeEndActions();
		} catch (RuntimeException ex) {
			msgCenter
					.fatal(
							"DefaultScheduleControllerAction.execute: RunTimeException when running the schedule\n"
									+ "Current tick ("
									+ (schedule != null ? String
											.valueOf(schedule.getTickCount())
											: "unavailable") + ")", ex);
		}
	}

	protected ISchedule getSchedule(RunState toExecuteOn) {
		return toExecuteOn.getScheduleRegistry().getModelSchedule();
	}
}
