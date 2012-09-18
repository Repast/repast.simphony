package repast.simphony.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.OutputAggregator;
import repast.simphony.batch.ssh.StatusException;
import simphony.util.messages.MessageCenter;

public class ClusterOutputCombiner {

	private static MessageCenter msg = MessageCenter.getMessageCenter(ClusterOutputCombiner.class);
	
	private String workingDir, outputDir;
	
	public ClusterOutputCombiner(String workingDir,String outputDir) throws FileNotFoundException, IOException{
		this.workingDir = workingDir;
		this.outputDir = outputDir;
		Properties props = new Properties();
	    File in = new File("MessageCenter.log4j.properties");
	    props.load(new FileInputStream(in));
	    PropertyConfigurator.configure(props);
	}

	public void run() {
		try {
			List<File> files = findOutput(workingDir);
			OutputAggregator aggregator = new OutputAggregator();
			new File(outputDir).mkdirs();
			aggregator.run(files, outputDir);
			msg.info("Aggregating output into " + outputDir);
		} catch (StatusException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public List<File> findOutput(String directory) throws StatusException {
		LocalOutputFinder finder = new LocalOutputFinder();
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
	public static void main(String[] args) throws FileNotFoundException, IOException {
		new ClusterOutputCombiner(args[0],args[1]).run();
	}

}
