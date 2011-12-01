package repast.simphony.batch;

/**
 * Interface for classes that can create BatchScenarios. Users will
 * implement this class in order to have their BatchScenario run.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface BatchScenarioCreator {

	/**
	 * Creates a BatchScenario suitable for running by the BatchRunner.
	 *
	 * @return a BatchScenario suitable for running by the BatchRunner.
	 */
	BatchScenario createScenario();
}
