package repast.simphony.batch;

import repast.simphony.engine.controller.TickListener;
import repast.simphony.engine.environment.AbstractRunner;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ISchedule;
import simphony.util.messages.MessageCenter;

/**
 * Modified version of BatchScheduleRunner that lets the user
 * programmatically call the scheduler  
 * 
 * @author Nick Collier
 * @author Eric Tatara
 */
public class InteractivBatchRunner extends AbstractRunner {

	private static final MessageCenter msgCenter = MessageCenter
	.getMessageCenter(InteractivBatchRunner.class);

	private TickListener tickListener = null;
	
	private Object monitor = new Object();

	private boolean step = false;

	class ScheduleLoopRunnable implements Runnable {

		private ISchedule schedule;

		public ScheduleLoopRunnable(ISchedule schedule) {
			this.schedule = schedule;
		}

		public void run() {
			fireStartedMessage();
			try {
				while (go() && schedule.getActionCount() > 0) {
					if (schedule.getModelActionCount() == 0) {
						schedule.setFinishing(true);
					}
					schedule.execute();
					if (step) {
						pause = true;
						step = false;
					}
					if (tickListener != null) {
						tickListener.tickCountUpdated(schedule.getTickCount());
					}
				}
				schedule.executeEndActions();
			} catch (RuntimeException ex) {
				msgCenter.fatal("RunTimeException when running the schedule\n"
						+ "Current tick ("
						+ (schedule != null ? String.valueOf(schedule.getTickCount())
								: "unavailable") + ")", ex);
			}
			fireStoppedMessage();
		}
	}

	/**
	 * This executes the given RunState object's schedule. It will continue executing the schedule
	 * until there are no more actions schedule or the run manager tells it to stop.
	 * 
	 * @param toExecuteOn
	 *            the RunState to execute on
	 */
	public void execute(RunState toExecuteOn) {
		ISchedule schedule = getSchedule(toExecuteOn);
		Runnable runnable = new ScheduleLoopRunnable(schedule);
		Thread thread = new Thread(runnable);
		thread.start();
	}

	private void notifyMonitor() {
		synchronized (monitor) {
			monitor.notify();
		}
	}

	/**
	 * 
	 * @return true if the runner should keep going
	 */
	@Override
	public boolean go() {
		// check for and pause if necessary
		synchronized (monitor) {
			while (pause) {
				firePausedMessage();
				try {
					monitor.wait();
				} catch (InterruptedException e) {
					msgCenter.warn(
							"Caught InterruptedException when running simulation, unpausing.", e);

					fireRestartedMessage();
					// I think we need to break otherwise
					// we will be stuck in the loop
					break;
					// TODO: decide if we have to unpause in this condition
				}
				fireRestartedMessage();
			}
		}
		return !stop;
	}

	@Override
	public void setPause(boolean pause) {
		this.pause = pause;
		if (!pause) {
			notifyMonitor();
		}
	}

	@Override
	public void step() {
		step = true;
		if (pause) {
			pause = false;
			notifyMonitor();
		}
	}

	@Override
	public void stop() {
		stop = true;
		if (pause) {
			pause = false;
			notifyMonitor();
		}
	}

	public TickListener getTickListener() {
		return tickListener;
	}

	public void setTickListener(TickListener tickListener) {
		this.tickListener = tickListener;
	}

	protected ISchedule getSchedule(RunState toExecuteOn) {
		return toExecuteOn.getScheduleRegistry().getModelSchedule();
	}
}
