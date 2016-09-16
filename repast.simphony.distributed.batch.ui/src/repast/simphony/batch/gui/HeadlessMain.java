/**
 * 
 */
package repast.simphony.batch.gui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.swing.DefaultListModel;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.xml.sax.SAXException;

import repast.simphony.batch.gui.Host.Type;
import repast.simphony.batch.parameter.ParametersToInput;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.ConsoleUserInfo;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.SessionsDriver;
import repast.simphony.batch.standalone.StandAloneMain;

/**
 * @author Jonathan Ozik
 */
public class HeadlessMain {

	private BatchRunConfigBean model;

	public HeadlessMain(BatchRunConfigBean model) {
		this.model = model;
	}

	private static Logger logger = Logger.getLogger(HeadlessMain.class);

	public void run(boolean run, String upf) {

		// Validation
		ValidationResult result = validateModel();

		// Archive or run
		if (result.equals(ValidationResult.SUCCESS)) {
			try {
				File configFile = new File(model.getOutputDirectory(), "config.props");
				writeConfigFile(configFile);
				Project p = createAntProject(upf);
				p.executeTarget(p.getDefaultTarget());
				if (run) {
					SessionsDriver driver = new SessionsDriver(configFile.getCanonicalPath());
					SSHSessionFactory.getInstance().setUserInfo(new ConsoleUserInfo());
					driver.run();
				}
			} catch (IOException | ParserConfigurationException | SAXException ex) {
				logger.error("Error encountered", ex);
			}
		} else {
			logger.error(result.getMessage());
		}
		// Make sure output is logged (look at repast.simphony.batch logging)
		// Override upf file generation if upf option is sent in
	}

	private Project createAntProject(String upf) throws IOException, ParserConfigurationException, SAXException {

		Project project = new Project();
		@SuppressWarnings("restriction")
		URL url = groovy.lang.GroovyObject.class.getProtectionDomain().getCodeSource().getLocation();
		project.setProperty("groovy.home", URLDecoder.decode(url.getFile(), "UTF-8"));

		url = SessionsDriver.class.getResource("/scripts/build.xml");
		String antFile = new File(URLDecoder.decode(url.getFile(), "UTF-8")).getCanonicalPath();
		int index = antFile.indexOf("repast.simphony.distributed.batch_");
		if (index != -1) {
			int start = index + "repast.simphony.distributed.batch_".length();
			int end = antFile.indexOf(File.separator, start);
			String version = antFile.substring(start, end);
			project.setProperty("plugins.version", version);
			project.setUserProperty("plugins.version", version);
		} else {
			project.setProperty("plugins.version", "");
			project.setUserProperty("plugins.version", "");
		}

		project.setUserProperty("ant.file", antFile);
		project.setProperty("model.dir", new File(model.getModelDirectory()).getCanonicalPath());
		project.setProperty("model.scenario.dir", new File(model.getScenarioDirectory()).getCanonicalPath());
		project.setProperty("working.dir",
				new File(System.getProperty("java.io.tmpdir"), "working").getCanonicalPath());
		File batchParamFile = new File(model.getBatchParameterFile());
		File unrolledParamFile = null;
		if (upf.isEmpty()) {
			unrolledParamFile = new File(System.getProperty("java.io.tmpdir"), "unrolledParamFile.txt");
			ParametersToInput pti = new ParametersToInput(batchParamFile);
			File batchMapFile = new File(System.getProperty("java.io.tmpdir"), "batchMapFile.txt");
			pti.formatForInput(unrolledParamFile, batchMapFile);
			logger.info(String.format("Unrolling batch parameter file:\n\t%s to\n\t%s", batchParamFile.getPath(),
					unrolledParamFile.getPath()));
		} else {
			unrolledParamFile = new File(upf);
			logger.info(String.format("Unrolled parameter file provided:\n\t%s", unrolledParamFile.getPath()));
		}

		project.setProperty("unrolled.param.file", unrolledParamFile.getCanonicalPath());
		project.setProperty("config.props.file",
				new File(model.getOutputDirectory(), "config.props").getCanonicalPath());
		project.setProperty("batch.param.file", batchParamFile.getCanonicalPath());

		File output = new File(model.getOutputDirectory());
		if (!output.exists())
			output.mkdirs();

		// project.setProperty("zip.file", new File(output,
		// "complete_model.zip").getCanonicalPath());
		project.setProperty("jar.file", new File(output, "complete_model.jar").getCanonicalPath());

		project.init();
		ProjectHelper helper = ProjectHelper.getProjectHelper();
		project.addReference("ant.projectHelper", helper);
		helper.parse(project, new File(URLDecoder.decode(url.getFile(), "UTF-8")));

		// TODO: make sure that this makes it to stdout and stderr
		DefaultLogger listener = new DefaultLogger();
		listener.setErrorPrintStream(System.err);
	    listener.setOutputPrintStream(System.out);
	    listener.setMessageOutputLevel(Project.MSG_INFO);
		project.addBuildListener(listener);

		logger.info("plugins version: " + project.getProperty("plugins.version"));

		return project;
	}

	public ValidationResult validateModel() {
		if (model.getModelDirectory().trim().isEmpty())
			return new ValidationResult("Model directory is missing.");
		if (model.getScenarioDirectory().trim().isEmpty())
			return new ValidationResult("Scenario directory is missing.");
		if (model.getOutputDirectory().trim().isEmpty())
			return new ValidationResult("Ouput directory is missing.");
		if (model.getKeyDirectory().trim().isEmpty())
			return new ValidationResult("SSH key directory is missing.");

		for (OutputPattern pattern : model.getOutputPatterns()) {
			if (pattern.getPattern().trim().length() == 0 || pattern.getPath().trim().length() == 0)
				return new ValidationResult("Invalid output file pattern: pattern is missing path or pattern");
		}

		if (model.getHosts().isEmpty()) {
			return new ValidationResult("Hosts list is empty.");
		}

		return ValidationResult.SUCCESS;
	}

	private void writeConfigFile(File configFile) throws IOException {
		// create the config.properties file
		BufferedWriter writer = null;
		try {
			configFile.getParentFile().mkdirs();
			writer = new BufferedWriter(new FileWriter(configFile));
			writer.write(Configuration.MA_KEY + " = "
					+ convertPath(new File(model.getOutputDirectory(), "complete_model.jar").getCanonicalPath())
					+ "\n");
			writer.write(Configuration.BATCH_PARAMS_KEY + " = scenario.rs/batch_params.xml\n");
			writer.write(Configuration.SSH_DIR_KEY + " = "
					+ convertPath(new File(model.getKeyDirectory()).getCanonicalPath()) + "\n");
			// stored in minutes, but config in seconds
			writer.write(Configuration.POLL_INTERVAL_KEY + " = " + model.getPollFrequency() * 60 + "\n");
			writer.write(Configuration.OUT_DIR_KEY + " = "
					+ convertPath(new File(model.getOutputDirectory()).getCanonicalPath()) + "\n\n");
			writer.write(Configuration.VM_ARGS_KEY + " = " + model.getVMArguments() + "\n");
			writeHosts(writer);

			int i = 1;
			for (OutputPattern pattern : model.getOutputPatterns()) {
				writer.write(
						Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATH + " = " + pattern.getPath());
				writer.write("\n");
				writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATTERN + " = "
						+ pattern.getPattern());
				writer.write("\n");
				writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.CONCATENATE + " = "
						+ String.valueOf(pattern.isConcatenate()));
				writer.write("\n");
				writer.write(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.HEADER + " = "
						+ String.valueOf(pattern.isHeader()));
				writer.write("\n");
				i++;
			}

			logger.info("Writing batch run config file to: " + configFile.getAbsolutePath());
		} finally {
			if (writer != null)
				writer.close();
		}
	}

	private String convertPath(String path) {
		return path.replace("\\", "/");
	}

	public void writeHosts(Writer writer) throws IOException {

		List<Host> listModel = model.getHosts();
		for (int i = 0; i < listModel.size(); i++) {
			Host host = listModel.get(i);
			if (host.getType() == Type.LOCAL) {
				writer.write(Configuration.LOCAL_PREFIX + "." + i + "." + Configuration.SESSION_INSTANCES + " = "
						+ host.getInstances() + "\n");

				writer.write(Configuration.LOCAL_PREFIX + "." + i + ".working_directory = "
						+ System.getProperty("java.io.tmpdir", "model_run").replace("\\", "/") + "\n");

			} else {
				String prefix = Configuration.REMOTE_PREFIX + "." + i + ".";
				writer.write(prefix + Configuration.SESSION_USER + " = " + host.getUser() + "\n");
				writer.write(prefix + Configuration.SESSION_HOST + " = " + host.getHost() + "\n");
				writer.write(prefix + Configuration.SESSION_INSTANCES + " = " + host.getInstances() + "\n");
				writer.write(prefix + Configuration.SESSION_KEY_FILE + " = " + host.getSSHKeyFile().replace("\\", "/")
						+ "\n");
			}
		}
	}

	public static void main(final String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = StandAloneMain.getOptions();
		options.addOption(StandAloneMain.PROPS_FILE,true, "location of the log4j properties file");
		try {
			CommandLine line = parser.parse(options, args);

			String propsFile = "";
			if (line.hasOption(StandAloneMain.PROPS_FILE)) {
				propsFile = (line.getOptionValue(StandAloneMain.PROPS_FILE));
			}
			if (propsFile.length() == 0)
				propsFile = "../repast.simphony.distributed.batch/config/SSH.MessageCenter.log4j.properties";

			Properties props = new Properties();
			File in = new File(propsFile);
			props.load(new FileInputStream(in));
			PropertyConfigurator.configure(props);

			// Create bean
			BatchRunConfigBean model = new BatchRunConfigBean();
			// Check for properties file first
			if (line.hasOption("c")) {
				model.load(new File(line.getOptionValue("c")));
			}
			// Find overriding elements
			if (line.hasOption("p")) {
				model.setParameterFile(line.getOptionValue("p"));
			}
			if (line.hasOption("b")) {
				model.setBatchParameterFile(line.getOptionValue("b"));
			}
			if (line.hasOption(StandAloneMain.MODEL_DIR)) {
				model.setModelDirectory(line.getOptionValue(StandAloneMain.MODEL_DIR));
			}
			if (line.hasOption("s")) {
				model.setScenarioDirectory(line.getOptionValue("s"));
			}
			if (line.hasOption("o")) {
				model.setOutputDirectory(line.getOptionValue("o"));
			}

			String upf = "";
			if (line.hasOption("u")) {
				upf = line.getOptionValue("u");
			}

			boolean run = false;
			if (line.hasOption("r")) {
				if (line.hasOption("u")) {
					System.err.println("Option -r,--run is incompatible with option -u,--upf, exiting.");
					return;
				}
				run = true;
			}

			new HeadlessMain(model).run(run, upf);

		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}

	}

}
