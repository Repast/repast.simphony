package repast.simphony.parameter.optimizer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import repast.simphony.parameter.RunResultProducer;

/**
 * Parses the properties from an optimizing properties file.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class OptPropertiesParser {

	private static final String RUN_RESULT_PRODUCER = "run_result_producer";
	private static final String PARAMS_FILE = "parameter_file";
	private static final String BEAN_SHELL_SCRIPT = "bean_shell_script";
	private static final String ADVANCEMENT_CHOOSER = "advancement_chooser";
	private Properties props;


	/**
	 * Creates a properites parser to parse the specified file.
	 *
	 * @param file
	 * @throws IOException
	 */
	public OptPropertiesParser(File file) throws IOException {
		props = new Properties();
		FileInputStream inStream = new FileInputStream(file);
		props.load(inStream);
		inStream.close();

		// make sure have enough props to run
		if (!props.containsKey(RUN_RESULT_PRODUCER)) {
			throw new IOException("Properties file must specify a RunResultProducer implementation");
		}

		if (!(props.containsKey(PARAMS_FILE) || props.containsKey(BEAN_SHELL_SCRIPT))) {
			throw new IOException("Properties file must specify a parameters file or a bean shell " +
							"framework style script");
		}
	}

	/**
	 * Gets the RunResultProducer from the class name in the properties file.
	 *
	 * @return the RunResultProducer from the class name in the properties file.
	 *
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public RunResultProducer getRunResultProducer() throws ClassNotFoundException, IllegalAccessException,
					InstantiationException {
		String runResultProducer = props.getProperty(RUN_RESULT_PRODUCER);
		Class clazz = Class.forName(runResultProducer);
		return (RunResultProducer) clazz.newInstance();
	}

	/**
	 * Gets the AdvancementChooser (if any) from the class name in the properties file.
	 *
	 * @return the AdvancementChooser (if any) from the class name in the properties file.
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AdvancementChooser getAdvancementChooser() throws ClassNotFoundException,
					IllegalAccessException, InstantiationException {
		String chooser = props.getProperty(ADVANCEMENT_CHOOSER);
		if (chooser != null) {
			Class clazz = Class.forName(chooser);
			return (AdvancementChooser) clazz.newInstance();
		}
		return null;
	}

	/**
	 * Gets the name of the parameters file to use for this optimized sweep.
	 *
	 * @return the name of the parameters file to use for this optimized sweep.
	 */
	public String getParametersFile() {
		return props.getProperty(PARAMS_FILE);
	}

	/**
	 * Gets the name of the BSF script file to use for this optimized sweep.
	 *
	 * @return the name of the BSF script file to use for this optimized sweep.
	 */
	public String getBSFScript() {
		return props.getProperty(BEAN_SHELL_SCRIPT);
	}
}
