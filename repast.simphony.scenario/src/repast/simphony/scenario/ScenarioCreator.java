package repast.simphony.scenario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.ParseErrorException;

import repast.simphony.util.FileUtils;
import simphony.util.messages.MessageCenter;

/**
 * Creates a scenario.xml file.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScenarioCreator {

	private static MessageCenter msgCenter = MessageCenter.getMessageCenter(ScenarioCreator.class);

	private File modelPath, scenarioPath;
	private String modelInitializerClass;
	private boolean useModelPlugin;

	private List<ScenarioEntry> entries = new ArrayList<ScenarioEntry>();
	private List<ScenarioCreatorExtension> plugins = new ArrayList<ScenarioCreatorExtension>();

	static Set<ScenarioCreatorExtension> extensions = new HashSet<ScenarioCreatorExtension>();

	/**
	 * Statically register a strategy. The strategy will be added to any
	 * instance of a ScenarioCreator when it is created.
	 *
	 * @param extension the strategy to register.
	 */
	public static void registerExtension(ScenarioCreatorExtension extension) {
		extensions.add(extension);
	}

	/**
	 * Creates a ScenarioCreator that will create a scenario.xml in the specified scenario path,
	 * copying a model.score file from the model path.
	 *
	 * @param modelPath
	 * @param scenarioPath
	 */
	public ScenarioCreator(File modelPath, File scenarioPath) {
		this(modelPath, scenarioPath, null, false);
	}

	/**
	 * Creates a ScenarioCreator that will create a scenario.xml in the specified scenario path,
	 * copying a model.score file from the model path and using the specified modelInitializer.
	 *
	 * @param modelPath
	 * @param scenarioPath
	 * @param modelInitializerClass
	 */
	public ScenarioCreator(File modelPath, File scenarioPath, String modelInitializerClass) {
		this(modelPath, scenarioPath, modelInitializerClass, false);
	}

	/**
	 * Creates a ScenarioCreator that will create a scenario.xml in the specified scenario path,
	 * copying a model.score file from the model path and using the specified modelInitializer.
	 *
	 * @param modelPath
	 * @param scenarioPath
	 * @param useModelPlugin if true, a model plugin entry is added to the scenario.xml file.
	 */
	public ScenarioCreator(File modelPath, File scenarioPath, boolean useModelPlugin) {
		this(modelPath, scenarioPath, null, useModelPlugin);
	}


	/**
	 * Creates a ScenarioCreator that will create a scenario.xml in the specified scenario path,
	 * copying a model.score file from the model path and using the specified modelInitializer.
	 *
	 * @param modelPath
	 * @param scenarioPath
	 * @param modelInitializerClass
	 * @param useModelPlugin        if true, a model plugin entry is added to the scenario.xml file.
	 */
	public ScenarioCreator(File modelPath, File scenarioPath, String modelInitializerClass,
	                       boolean useModelPlugin) {
		this.modelInitializerClass = modelInitializerClass;
		this.modelPath = modelPath;
		this.scenarioPath = scenarioPath;
		this.useModelPlugin = useModelPlugin;
		for (ScenarioCreatorExtension extension : extensions) {
			addExtension(extension);
		}
	}

	private boolean containsExtensionType(ScenarioCreatorExtension extension) {
		for (ScenarioCreatorExtension strat : plugins) {
			if (extension.getClass().equals(strat.getClass())) return true;
		}
		return false;
	}

	/**
	 * Adds the specified ScenarioCreatorPlugin that will be run
	 * during scenario creation.
	 *
	 * @param extension
	 */
	public void addExtension(ScenarioCreatorExtension extension) {
		if (!containsExtensionType(extension)) plugins.add(extension);
	}

	/**
	 * Create the scenario.
	 *
	 * @throws Exception
	 * @throws ParseErrorException
	 */
	public void createScenario() throws Exception, ParseErrorException {
		copyScoreFile();
		runExtensions();

		VelocityContext context = new VelocityContext();
		String template = getClass().getPackage().getName();
		template = template.replace('.', '/');
		template = template + "/Scenario.vt";
		context.put("entries", entries);

		if (modelInitializerClass != null && modelInitializerClass.length() > 0)
			context.put("modelInit", modelInitializerClass);
		context.put("modelPlugin", useModelPlugin);

		Writer writer = new FileWriter(new File(scenarioPath, Scenario.SCENARIO_FILE_NAME));
		Velocity.mergeTemplate(template, "UTF-8", context, writer);
		writer.close();
	}

	private void runExtensions() {
		for (ScenarioCreatorExtension extension : plugins) {
			try {
				List<ScenarioEntry> entry = extension.run(scenarioPath);
				if (entry != null) entries.addAll(entry);
			} catch (Exception ex) {
				msgCenter.error("Error while running " + extension.getClass().getName(), ex);
			}
		}
	}

	private void copyScoreFile() throws IOException {
		// only copy if the model is not already in the scenario directory
		if (!modelPath.getParentFile().equals(scenarioPath)) {
			FileUtils.copyFile(modelPath, new File(scenarioPath,
					ScenarioConstants.LEGACY_SCORE_FILE_NAME));
		}
	}
}
