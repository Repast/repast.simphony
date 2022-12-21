package repast.simphony.ui;

import javax.measure.Quantity;

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
		Quantity<?> timeQuantity = RunEnvironment.getInstance().getCurrentSchedule().getTimeQuantity();
		if (timeQuantity == null) {
			return builder.delete(12, builder.length()).append(tick).toString();
		} 
		else {
			Quantity<?> tickInQuantity = RunEnvironment.getInstance().getCurrentSchedule().getTickCountInTimeQuantity();
			double timeValue = Math.round(tickInQuantity.getValue().doubleValue() * precision) / ((double) precision);
			return ("Tick Count: " + timeValue + " " + timeQuantity.getUnit().toString());
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
