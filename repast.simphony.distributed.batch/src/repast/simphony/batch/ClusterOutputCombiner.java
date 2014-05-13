package repast.simphony.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.ssh.BaseOutputNamesFinder;
import repast.simphony.batch.ssh.BaseOutputNamesFinder.FinderOutput;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.DefaultOutputPatternCreator;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.MatchedFiles;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.StatusException;
import simphony.util.messages.MessageCenter;

public class ClusterOutputCombiner {

	private static MessageCenter msg = MessageCenter
			.getMessageCenter(ClusterOutputCombiner.class);

	private String workingDir, outputDir, configFile;

	public ClusterOutputCombiner(String workingDir, String outputDir)
			throws FileNotFoundException, IOException {
		this(workingDir,"config.props",outputDir);
	}

	public ClusterOutputCombiner(String workingDir, String configProps,
			String outputDir) throws FileNotFoundException, IOException {
		this.workingDir = workingDir;
		this.outputDir = outputDir;
		this.configFile = configProps;
		Properties props = new Properties();
		File in = new File("MessageCenter.log4j.properties");
		props.load(new FileInputStream(in));
		PropertyConfigurator.configure(props);
	}

	private List<OutputPattern> createPatterns() throws IOException,
			XMLStreamException {
		List<FinderOutput> fsFound = new BaseOutputNamesFinder().find("./scenario.rs");
		List<OutputPattern> patterns = new ArrayList<>();
		for (FinderOutput fs : fsFound) {
			DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator(fs.getFileName(), fs.hasTimestamp());
			// this has to be first, otherwise the non param map pattern will catch
			// it.
			patterns.add(creator.getParamMapPattern());
			patterns.add(creator.getFileSinkOutputPattern());
		}

		Configuration config = new Configuration(configFile);
		for (OutputPattern op : config.getOutputPatterns()) {
			patterns.add(op);
		}
		return patterns;
	}

	public void run() {
		try {
			List<MatchedFiles> files = findOutput(workingDir);
			new File(outputDir).mkdirs();
			msg.info("Aggregating output into " + outputDir);
			for (MatchedFiles file : files) {
				file.aggregateOutput(outputDir);
			}

		} catch (StatusException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (XMLStreamException e) {

			e.printStackTrace();
		}
	}

	public List<MatchedFiles> findOutput(String directory)
			throws StatusException, IOException, XMLStreamException {
		List<OutputPattern> filePatterns = createPatterns();
		LocalOutputFinder finder = new LocalOutputFinder();
		finder.addPatterns(filePatterns);

		File localDir = new File(directory);
		msg.info(String.format("Finding output on localhost in %s",
				localDir.getPath()));
		return finder.run(localDir);
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		new ClusterOutputCombiner(args[0], args[1]).run();
	}

}
