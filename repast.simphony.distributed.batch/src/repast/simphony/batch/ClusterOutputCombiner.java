package repast.simphony.batch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.PropertyConfigurator;

import repast.simphony.batch.ssh.BaseOutputNamesFinder;
import repast.simphony.batch.ssh.DefaultOutputPatternCreator;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.MatchedFiles;
import repast.simphony.batch.ssh.StatusException;
import simphony.util.messages.MessageCenter;

public class ClusterOutputCombiner {

  private static MessageCenter msg = MessageCenter.getMessageCenter(ClusterOutputCombiner.class);

  private String workingDir, outputDir;

  public ClusterOutputCombiner(String workingDir, String outputDir) throws FileNotFoundException,
      IOException {
    this.workingDir = workingDir;
    this.outputDir = outputDir;
    Properties props = new Properties();
    File in = new File("MessageCenter.log4j.properties");
    props.load(new FileInputStream(in));
    PropertyConfigurator.configure(props);
  }
  
  private List<Pair<String, String>> createPatterns() throws IOException, XMLStreamException {
    List<String> baseNames = new BaseOutputNamesFinder().find("./scenario.rs");
    List<Pair<String, String>> filePatterns = new ArrayList<Pair<String, String>>();
    for (String name : baseNames) {
      DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator(name);
      // this has to be first, otherwise the non param map pattern will catch it.
      filePatterns.add(Pair.of(creator.getFinalParamMapFileName(), creator.getParamMapPattern()));
      filePatterns.add(Pair.of(creator.getFinalFileName(), creator.getFilePattern()));
    }
    return filePatterns;
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

  public List<MatchedFiles> findOutput(String directory) throws StatusException, IOException, XMLStreamException {
    List<Pair<String, String>> filePatterns = createPatterns();
    LocalOutputFinder finder = new LocalOutputFinder();
    for (Pair<String, String> pattern : filePatterns) {
      finder.addPattern(pattern.getLeft(), pattern.getRight());
    }
    File localDir = new File(directory);
    msg.info(String.format("Finding output on localhost in %s", localDir.getPath()));
    return finder.run(localDir);
  }

  /**
   * @param args
   * @throws IOException
   * @throws FileNotFoundException
   */
  public static void main(String[] args) throws FileNotFoundException, IOException {
    new ClusterOutputCombiner(args[0], args[1]).run();
  }

}
