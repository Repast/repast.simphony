package repast.simphony.scenario;

import java.io.File;
import java.util.List;

/**
 * Interface for classes that want to add additional functionality to
 * the creation of a scenario file.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public interface ScenarioCreatorExtension {

	/**
	 * Perform some addition operation w/r to scenario file creation and return
	 * an optional ScenarioEntry that can be put in the scenario.xml file.
	 *
	 * @param scenarioPath the path to the scenario dir
	 * @return optional ScenarioEntry that can be put in the scenario.xml file. This can be null.
	 */
	List<ScenarioEntry> run(File scenarioPath) throws Exception;
}
