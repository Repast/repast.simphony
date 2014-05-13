package repast.simphony.batch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import repast.simphony.batch.ssh.BaseOutputNamesFinder;
import repast.simphony.batch.ssh.BaseOutputNamesFinder.FinderOutput;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.DefaultOutputPatternCreator;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.MatchedFiles;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.StatusException;

/**
 * Used to find output files from InstanceRunner runs using patterns from file
 * sinks and custom output patterns.
 * 
 * @author jozik
 * 
 */
public class ClusterOutputFinder {

  /**
   * Runs the cluster output finder.
   * 
   * @param instanceParentDir
   *          parent of instance directories
   * @param configFile
   *          config.props file
   * @param scenarioDir
   *          scenario.rs folder
   * @param outputFile
   *          output file containing all outputs relative to the
   *          instanceParentDir
   * @throws FileNotFoundException
   * @throws IOException
   * @throws StatusException
   * @throws XMLStreamException
   */
  public void run(String instanceParentDir, String configFile, String scenarioDir, String outputFile)
      throws FileNotFoundException, IOException, StatusException, XMLStreamException {
    BaseOutputNamesFinder bonFinder = new BaseOutputNamesFinder();
    List<FinderOutput> fsFound = bonFinder.find(scenarioDir);

    LocalOutputFinder finder = new LocalOutputFinder();
    for (FinderOutput fo : fsFound) {
      DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator(fo.getFileName(),
          fo.hasTimestamp());
      OutputPattern onePattern = creator.getFileSinkOutputPattern();
      OutputPattern oneBPPattern = creator.getParamMapPattern();
      finder.addPattern(oneBPPattern);
      finder.addPattern(onePattern);
    }

    Configuration config = new Configuration(configFile);
    List<OutputPattern> patterns = config.getOutputPatterns();
    for (OutputPattern op : patterns) {
      finder.addPattern(op);
    }

    File instanceParentDirFile = new File(instanceParentDir).getAbsoluteFile();
    Path instanceParentDirPath = instanceParentDirFile.toPath();
    List<MatchedFiles> files = finder.run(instanceParentDirFile);

    StringBuilder sb = new StringBuilder();
    for (MatchedFiles fs : files) {
      for (File file : fs.getFiles()) {
        Path relativePath = instanceParentDirPath.relativize(file.toPath());
        sb.append(relativePath);
        sb.append("\n");
      }
    }
    Charset charset = Charset.defaultCharset();
    String s = sb.toString();
    Path out = Paths.get(outputFile);
    try (BufferedWriter writer = Files.newBufferedWriter(out, charset)) {
      writer.write(s, 0, s.length());
    } catch (IOException x) {
      System.err.format("IOException: %s%n", x);
    }
  }

  public static void main(String[] args) throws FileNotFoundException, IOException,
      StatusException, XMLStreamException {
    new ClusterOutputFinder().run(args[0], args[1], args[2], args[3]);
  }

}
