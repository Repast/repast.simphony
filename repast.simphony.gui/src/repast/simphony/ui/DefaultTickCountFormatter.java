package repast.simphony.ui;

import org.jscience.physics.amount.Amount;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.ui.plugin.TickCountFormatter;

/**
 * Default implementation of a Tick Count formatter. This takes the tick value and returns the String
 * "Tick Count: N".
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DefaultTickCountFormatter implements TickCountFormatter {

	private StringBuilder builder = new StringBuilder("Tick Count: 0.0");
	
	private static long precision = 10000;

	/**
	 * Receives the current tick as an argument and returns a String. In this case, Given a tick N, the String
	 * will be "Tick Count: N"
	 *
	 * @param tick the tick to format
	 * @return a String representing the formatted tick.
	 */
	public String format(double tick) {
		Amount units = RunEnvironment.getInstance().getCurrentSchedule().getTimeUnits();
		if (units == null) {
			return builder.delete(12, builder.length()).append(tick).toString();
		} else {
			Amount tickInUnits = RunEnvironment.getInstance().getCurrentSchedule().getTickCountInTimeUnits();
			double timeValue = Math.round(tickInUnits.getEstimatedValue() * precision) / ((double) precision);
			return ("Tick Count: " + timeValue + " " + tickInUnits.getUnit().toString());
		}
	}

	/**
	 * Gets the initial text to display before the simulation clock has started.
	 *
	 * @return the initial text to display before the simulation clock has started.
	 */
	public String getInitialValue() {
		builder = new StringBuilder("Tick Count: 0.0");
		return builder.toString();
	}
}
