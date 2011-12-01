package repast.simphony.visualization;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract base class for LayoutUpdaters.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public abstract class AbstractLayoutUpdater implements LayoutUpdater {

	protected Layout layout;
	protected Set<Condition> conditions = new HashSet<Condition>();

	/**
	 * Creates a UpdateLayoutUpdater to manage the updates of the specified layout.
	 *
	 * @param layout
	 */
	public AbstractLayoutUpdater(Layout layout) {
		this.layout = layout;
	}


	/**
	 * Gets the layout whose updates this updater manages.
	 *
	 * @return the layout whose updates this updater manages.
	 */
	public Layout getLayout() {
		return layout;
	}

	/**
	 * Sets the layout whose updates this updater manages.
	 *
	 * @param layout
	 */
	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	/**
	 * @param conditions
	 * @return true if the current set of conditions contains one of the specified conditions.
	 */
	protected boolean hasCondition(Condition ... conditions) {
		for (Condition condition : conditions) {
			if (this.conditions.contains(condition)) return true;
		}
		return false;
	}

	/**
	 * Adds a trigger condition to the current set. This layout updater will update
	 * or not in response to whatever its current condition is.
	 *
	 * @param condition
	 */
	public void addTriggerCondition(Condition condition) {
		conditions.add(condition);
	}

}
