package repast.simphony.freezedry.wizard;

import repast.simphony.freezedry.FreezeDryedDataSource;

/**
 * In conjuction with a the gui, builds the action that creates a context.
 *
 * @author Jerry Vos
 */
public interface DataSourceBuilder {
	/**
	 * Gets an action that will create a freeze drying action.
	 *
	 * @param scenario
	 * @return an action that will create a context when run.
	 */
	FreezeDryedDataSource getDataSource();
}
