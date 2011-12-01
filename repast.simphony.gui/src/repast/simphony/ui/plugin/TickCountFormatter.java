package repast.simphony.ui.plugin;

/**
 * Interface for class that want to implement Tick Count formatting.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface TickCountFormatter {

	/**
	 * Receives the current tick as an argument and returns a String. For example, given a tick N, the String
	 * might be "Tick Count: N".
	 *
	 * @param tick the tick to format
	 * @return a String representing the formatted tick.
	 */
	String format(double tick);

	/**
	 * Gets the initial text to display before the simulation clock has started.
	 *
	 * @return the initial text to display before the simulation clock has started.
	 */
	String getInitialValue();
}
