package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import repast.simphony.batch.ssh.StatusException;

public class ClusterOutputFinderTests {

	List<String> expected = Arrays.asList(
			"instance_1/ModelOutput.2012.Aug.21.11_58_43.batch_param_map.txt",
			"instance_2/ModelOutput.2012.Aug.21.11_58_43.batch_param_map.txt",
			"instance_1/ModelOutput.2012.Aug.21.11_58_43.txt",
			"instance_2/ModelOutput.2012.Aug.21.11_58_43.txt",
			"instance_1/customOut1.txt", "instance_2/customOut345.txt",
			"instance_2/output/customOut66.txt", "instance_1/otherOut22.txt",
			"instance_2/otherOut3.txt" );

	@Test
	public void clusterOutputFinder() throws FileNotFoundException, IOException,
			StatusException, XMLStreamException {
		// String instanceParentDir, String configFile, String scenarioDir, String
		// outputFile
		Path outfile = Paths.get("./test_out/output.txt");
		Files.deleteIfExists(outfile);
		
		String instanceParentDir = "./test_data/for_testing_cluster_output";
		String configFile = "./test_data/test_config_with_patterns2.properties";
		String scenarioDir = "./test_data/test_scenario.rs";
		String outputFile = "./test_out/output.txt";
		new ClusterOutputFinder().run(instanceParentDir, configFile, scenarioDir,
				outputFile);

		
		List<String> lines = Files.readAllLines(outfile, Charset.defaultCharset());

		assertEquals(9,lines.size());
		for (String line : lines){
			assertTrue(expected.contains(line));
		}

		Files.delete(outfile);

	}

}
