package repast.simphony.scenario;

import java.io.File;

/**
 * Provides access to the scenario directory.
 * 
 * @author Eric Tatara
 *
 */
public class ScenarioUtils {

	private static File scenarioDir;

	public static File getScenarioDir() {
		return scenarioDir;
	}

	public static void setScenarioDir(File dir) {
		scenarioDir = dir;
	}
	
}
