package repast.simphony.dataLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.velocity.app.Velocity;
import org.java.plugin.PluginManager;

import repast.simphony.scenario.ScenarioCreator;
import repast.simphony.dataLoader.DataLoaderScenarioCreatorExtension;
import repast.simphony.plugin.ScenarioCreatorExtensions;
import repast.simphony.util.FileUtils;
import saf.core.runtime.Boot;

/**
 * Test of the DataLoaderScenarioCreatorPlugin. This needs to
 * be run from within plugins/repast.simphony.runtime
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class DLStrategyTest extends TestCase {

	private File modelPath = new File("../repast.simphony.score.runtime/test/source/model.score");
	private File modelExternalPath = new File("../repast.simphony.score.runtime/test/source/model_subcontext_external.score");
	private File modelLoadPath = new File("../repast.simphony.score.runtime/test/source/model_subcontext_load.score");
	private File scenarioPath = new File("../repast.simphony.score.runtime/test/target.rs");
	private ScenarioCreator creator;
	private File currentModel;

	static {
		try {
			Properties props = new Properties();
			props.put("resource.loader", "class");
			props.put("class.resource.loader.description", "Velocity Classpath Resource Loader");
			props.put("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			props.put("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.SimpleLog4JLogSystem");
			Velocity.init(props);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setUp() throws IOException {
		for (File file : scenarioPath.listFiles()) {
			file.delete();
		}
	}


	protected void tearDown() throws Exception {
		if (modelPath.exists()) modelPath.delete();
	}

	public void testJPFStyleCreator() {
		try {
			FileUtils.copyFile(modelLoadPath, modelPath);
			creator = new ScenarioCreator(modelPath, scenarioPath, true);
			Boot boot = new Boot();
			PluginManager manager = boot.init(new String[]{""});
			ScenarioCreatorExtensions ext = new ScenarioCreatorExtensions();
			ext.loadExtensions(manager);
			creator = new ScenarioCreator(modelPath, scenarioPath, true);
			creator.createScenario();
			// the data loader strategy should be added by the above
			// and so we should get the correct scenario.xml etc. file
			checkResults(true);

		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testCreator() {
		try {
			FileUtils.copyFile(modelLoadPath, modelPath);
			creator = new ScenarioCreator(modelPath, scenarioPath, true);
			creator.addExtension(new DataLoaderScenarioCreatorExtension());
			creator.createScenario();
			checkResults(true);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	public void testExternal() {
		try {
			FileUtils.copyFile(modelExternalPath, modelPath);
			creator = new ScenarioCreator(modelPath, scenarioPath, true);
			creator.addExtension(new DataLoaderScenarioCreatorExtension());
			creator.createScenario();
			checkResults(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	private void checkResults(boolean includeSubContext) throws IOException {
		String[] expected = null;
		if (includeSubContext) {
			expected = new String[]{"<Scenario>",
						"<repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context=\"All Traps\" " +
										"file=\"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_0.xml\" />",
						"<repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context=\"SubContext\" " +
										"file=\"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml\" />",
						"<model.plugin_jpf file=\"plugin_jpf.xml\" />",
						"</Scenario>"};
		} else {
			expected = new String[]{"<Scenario>",
						"<repast.simphony.dataLoader.engine.ClassNameDataLoaderAction context=\"All Traps\" " +
										"file=\"repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_0.xml\" />",
						"<model.plugin_jpf file=\"plugin_jpf.xml\" />",
						"</Scenario>"};
		}
		assertTrue(new File(scenarioPath, "model.score").exists());
		assertTrue(new File(scenarioPath, "scenario.xml").exists());
		assertTrue(new File(scenarioPath, "repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_0.xml").exists());
		if (includeSubContext)
			assertTrue(new File(scenarioPath, "repast.simphony.dataLoader.engine.ClassNameDataLoaderAction_1.xml").exists());
		Set<String> lines = readScenario();
		for (String line : expected) {
			assertTrue(line + " not found", lines.contains(line));
		}
	}

	private Set<String> readScenario() throws IOException {
		Set<String> set = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new FileReader(new File(scenarioPath, "scenario.xml")));
		String line = null;
		while ((line = reader.readLine()) != null) {
			set.add(line);
		}

		return set;
	}
}
