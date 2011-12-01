package repast.simphony.parameter.bsf;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.bsf.BSFException;
import org.apache.bsf.BSFManager;

import repast.simphony.engine.environment.ControllerRegistry;
import repast.simphony.parameter.optimizer.AdvancementChooser;
import repast.simphony.parameter.optimizer.OptimizedParameterSweeper;
import repast.simphony.parameter.Parameters;
import repast.simphony.parameter.*;

/**
 * Runs a bsf script to set up a parameter sweep. This
 * puts a ParameterSweepBuilder with the variable name 'builder'
 * into the script's execution space.
 *
 * @author Nick Collier
 * @version $Revision$ $Date$
 */
public class ScriptRunner implements SweeperProducer {

	private ParameterSweeperBuilder builder;
	private File file;
	private RunResultProducer producer;
	private AdvancementChooser chooser = new OptimizedParameterSweeper.DefaultAdvanceChooser();
	private boolean run = true;

	/**
	 * Creates a ScriptRunner to run the specified file
	 * using the specified RunResultProducer to optimize
	 * the parameters.
	 *
	 * @param producer the producer to use
	 * @param file     the file to run
	 */
	public ScriptRunner(RunResultProducer producer, File file) {
		this.file = file;
		this.producer = producer;
	}

	/**
	 * Creates a ScriptRunner to run the specified file
	 * using the specified RunResultProducer, and AdvancementChooser to optimize
	 * the parameters.
	 *
	 * @param producer the producer to use
	 * @param chooser the chooser to use
	 * @param file     the file to run
	 */
	public ScriptRunner(RunResultProducer producer, AdvancementChooser chooser, File file) {
		this.file = file;
		this.producer = producer;
		this.chooser = chooser;
	}


	/**
	 * Creates a ScriptRunner to run the specified file.
	 *
	 * @param file the file to run
	 */
	public ScriptRunner(File file) {
		this.file = file;
	}

	/**
	 * Initializes the producer with the controller registry and
	 * master context id.
	 *
	 * @param registry
	 * @param masterContextId
	 */
	public void init(ControllerRegistry registry, Object masterContextId) {
		if (producer == null) builder = new ParameterSweeperBuilder();
		else {
			OptimizedParameterSweeper sweeper = new OptimizedParameterSweeper(registry, masterContextId);
			sweeper.setResultProducer(producer);
			sweeper.setAdvancementChooser(chooser);
			builder = new ParameterSweeperBuilder(sweeper);
		}
	}

	/**
	 * Runs the specified file as a bsf script. This
	 * puts a ParameterSweepBuilder with the variable name 'builder'
	 * into the script's execution space.
	 *
	 * @throws IOException
	 */
	private void run() throws IOException {
		try {
			String lang = BSFManager.getLangFromFilename(file.getAbsolutePath());
			BSFManager manager = new BSFManager();
			manager.declareBean("builder", builder, builder.getClass());
			BufferedReader reader = new BufferedReader(new FileReader(file));
			StringBuilder str = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				str.append(line);
				str.append("\n");
			}
			reader.close();

			manager.exec(lang, file.getAbsolutePath(), 1, 1, str.toString());
		} catch (BSFException e) {
			IOException ex = new IOException("Error running script file");
			ex.initCause(e);
			throw ex;
		}
		run = false;
	}

	/**
	 * Gets the Parameters created by the script via the ParameterSweeperBuilder.
	 *
	 * @return the Parameters created by the script via the ParameterSweeperBuilder.
	 */
	public Parameters getParameters() throws IOException {
		if (run) {
			run();
		}
		return builder.getParameters();
	}

	/**
	 * Gets the ParameterTreeSweeper reated by the script via the ParameterSweeperBuilder.
	 *
	 * @return the ParameterTreeSweeper reated by the script via the ParameterSweeperBuilder.
	 */
	public ParameterTreeSweeper getParameterSweeper() throws IOException {
		if (run) {
			run();
		}
		return builder.getSweeper();
	}
}
