package repast.simphony.ui;

import repast.simphony.engine.environment.RunEnvironment;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Model for getting and setting pause, end times, and sparkline length via the
 * gui.
 * 
 * @author Nick Collier
 */
public class RunOptionsModel {

	private PropertyChangeSupport support;
	private double pauseAt, stopAt;
	private int sparklineLength;
	private boolean sparklineType;
	private int scheduleTickDelay;

	public RunOptionsModel() {
		support = new PropertyChangeSupport(this);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}

	/**
	 * Gets the pause at tick.
	 * 
	 * @return the pause at tick.
	 */
	public double getPauseAt() {
		return pauseAt;
	}

	/**
	 * Notifies this RunOptions model that simulation has started. This will
	 * commit the current values.
	 */
	public void simStarted() {
		commit();
	}

	/**
	 * Notifies this RunOptions model that simulation has stoped.
	 */
	public void simStopped() {
	}

	private void commit() {
		RunEnvironment re = RunEnvironment.getInstance();
		
		if (re != null) {
			if (pauseAt > 0) 
				re.pauseAt(pauseAt);
			
			if (stopAt > 0)  
				re.endAt(stopAt);
			
			if (sparklineLength > 0) 
				re.setSparklineLength(sparklineLength);
			
			re.setSparklineType(sparklineType);
			
			re.setScheduleTickDelay(scheduleTickDelay);
		}
	}

	/**
	 * Sets the pause at tick.
	 * 
	 * @param pauseAt
	 *            the tick to pause at
	 */
	public void setPauseAt(double pauseAt) {
		if (pauseAt != this.pauseAt) {
			double old = this.pauseAt;
			this.pauseAt = pauseAt;
			commit();
			support.firePropertyChange("pauseAt", old, pauseAt);
		}
	}

	/**
	 * Gets the tick to stop at.
	 * 
	 * @return the tick to stop at.
	 */
	public double getStopAt() {
		return stopAt;
	}

	/**
	 * Sets the tick to stop at
	 * 
	 * @param stopAt
	 *            the tick to stop at
	 */
	public void setStopAt(double stopAt) {
		if (stopAt != this.stopAt) {
			double old = this.stopAt;
			this.stopAt = stopAt;
			commit();
			support.firePropertyChange("stopAt", old, stopAt);
		}
	}

	/**
	 * The sparkline length.
	 * 
	 * @return the sparkline length.
	 */
	public int getSparklineLength() {
		return sparklineLength;
	}

	/**
	 * Sets the sparkline length
	 * 
	 * @param sparklineLength
	 *            the sparkline length
	 */
	public void setSparklineLength(int sparklineLength) {
		if (sparklineLength != this.sparklineLength) {
			double old = this.sparklineLength;
			this.sparklineLength = sparklineLength;
			commit();
			support.firePropertyChange("sparklineLength", old, sparklineLength);
		}
	}

	/**
	 * The sparkline type.
	 * 
	 * @return the sparklineType type.
	 */
	public boolean getSparklineType() {
		return sparklineType;
	}

	/**
	 * Sets the sparkline type
	 * 
	 * @param sparklineType
	 *            the sparkline type
	 */
	public void setSparklineType(boolean sparklineType) {
		if (sparklineType != this.sparklineType) {
			boolean old = this.sparklineType;
			this.sparklineType = sparklineType;
			commit();
			support.firePropertyChange("sparklineType", old, sparklineType);
		}
	}
	
	/**
	 * The schedule tick delay.
	 * 
	 * @return the schedule tick delay.
	 */
	public int getScheduleTickDelay() {
		return scheduleTickDelay;
	}

	/**
	 * Set the Schedule tick delay.
	 * 
	 * @param scheduleTickDelay the schedule tick delay.
	 */
	public void setScheduleTickDelay(int scheduleTickDelay) {
		if (scheduleTickDelay != this.scheduleTickDelay) {
			double old = this.scheduleTickDelay;
			this.scheduleTickDelay = scheduleTickDelay;
			commit();
			support.firePropertyChange("scheduleTickDelay", old, scheduleTickDelay);
		}
	}
	
}
