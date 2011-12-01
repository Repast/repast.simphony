package repast.simphony.gis.legend;

/**
 * An action that can be executed on an entry in a legend.
 *
 * @author $Author: howe $
 * @version $Revision: 1.4 $
 * @date Oct 24, 2006
 */
public interface LegendAction<T extends LegendEntry> {

	/**
	 * Execute the action on the legend entry.
	 * 
	 * @param entry
	 *            The entry on which to execute this action
	 */
	public void execute(T entry);

	/**
	 * Does this legend action support the entry being passed to it.
	 *
	 * @param entry
	 *            The entry to act upon
	 * @return True if this action can support the entry otherwise false.
	 */
	public boolean canProcess(LegendEntry entry);

}
