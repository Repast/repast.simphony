/**
 * 
 */
package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.batch.ssh.BaseOutputNamesFinder;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.DefaultOutputPatternCreator;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.MatchedFiles;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.RemoteSession;
import repast.simphony.batch.ssh.RemoteStatusCopier;
import repast.simphony.batch.ssh.RemoteStatusGetter;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.Session;
import repast.simphony.batch.ssh.StatusException;
import repast.simphony.data2.engine.FileSinkControllerActionIO;

/**
 * @author Nick Collier
 */
public class RemoteOutputTest {

  private static final String REMOTE_DIR = "for_testing_simphony_model";
  
  private static final String[] NO_HEADER_OUTPUT = {"instance_4, output", "instance_3, output",
    "instance_1, output", "instance_2, output"
  };

  private Set<File> expectedFiles = new HashSet<File>();

  static {
    PropertyConfigurator.configure("./config/SSH.MessageCenter.log4j.properties");
    String home = System.getProperty("user.home");
    SSHSessionFactory.init(home + "/.ssh");
  }

  @Before
  public void setUp() {
    File top = new File("./test_data/" + REMOTE_DIR);
    for (int i = 1; i < 5; i++) {
      File dir = new File(top, "instance_" + i);
      for (File file : dir.listFiles()) {
        expectedFiles.add(file);
      }
    }
  }

  private void getLines(Set<String> lines, String file) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.trim().length() > 0)
          lines.add(line.trim());
      }
    } finally {
      if (reader != null)
        reader.close();
    }
  }
  
  @Test
  public void testBaseNameFinder() throws IOException, XMLStreamException {
    List<String> names = new BaseOutputNamesFinder().find("./test_data/test_scenario.rs");
    assertEquals(2, names.size());
    Set<String> expected = new HashSet<String>();
    expected.add("ModelOutput.txt");
    expected.add("ModelOutput2.txt");
    
    for (String name : names) {
      assertTrue(expected.remove(name));
    }
  }
  
  @Test
  public void testBaseNameFinderZip() throws IOException, XMLStreamException {
    
    // base output names.
    BaseOutputNamesFinder finder = new BaseOutputNamesFinder();
    List<String> baseNames = new ArrayList<String>();
    ZipFile zip = new ZipFile("./test_data/test_scenario.rs/complete_model.zip");
    for (Enumeration<? extends ZipEntry> iter = zip.entries(); iter.hasMoreElements();) {
      ZipEntry entry = iter.nextElement();
      if (entry.getName().startsWith("scenario.rs/" + FileSinkControllerActionIO.SERIALIZATION_ID)) {
        baseNames.add(finder.find(zip.getInputStream(entry)));
      }
    }
    zip.close();
    assertEquals(1, baseNames.size());
    assertEquals("ModelOutput.txt", baseNames.get(0));
  }
  
  @Test
  public void testAggregationNoHeader() throws StatusException, IOException {
    File f = new File("./test_out/no_header.txt");
    if (f.exists()) f.delete();
    
    LocalOutputFinder finder = new LocalOutputFinder();
    OutputPattern pattern = new OutputPattern();
    pattern.setPattern("MyCustomOutput.txt");
    pattern.setConcatenate(true);
    pattern.setPath("no_header.txt");
    pattern.setHeader(false);
    finder.addPattern(pattern);
    
    List<MatchedFiles> files = finder.run(new File("./test_data/for_testing_simphony_model"));
    assertEquals(1, files.size());
    files.get(0).aggregateOutput("./test_out");
    f = new File("./test_out/no_header.txt");
    
    assertTrue(f.exists());
    Set<String> expected = new HashSet<>(Arrays.asList(NO_HEADER_OUTPUT));
    try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
      String line = null;
      while ((line = reader.readLine()) != null) {
        if (line.trim().length() > 0)
        assertTrue(expected.remove(line));
      }
    }
    assertEquals(0, expected.size());
  }
  
  @Test
  public void testNoAggregation() throws StatusException, IOException {
    File f = new File("./test_out/custom output");
    if (f.exists()) {
      for (File file : f.listFiles()) {
        file.delete();
      }
      f.delete();
    }
    LocalOutputFinder finder = new LocalOutputFinder();
    OutputPattern pattern = new OutputPattern();
    pattern.setPattern("MyCustomOutput{2,}.txt");
    pattern.setConcatenate(false);
    pattern.setPath("custom output");
    pattern.setHeader(false);
    finder.addPattern(pattern);
    
    List<MatchedFiles> files = finder.run(new File("./test_data/for_testing_simphony_model"));
    assertEquals(1, files.size());
    files.get(0).aggregateOutput("./test_out");
    Path path = FileSystems.getDefault().getPath("./test_out/custom output");
    assertTrue(path.toFile().exists());
    assertTrue(path.toFile().isDirectory());
    assertEquals(8, path.toFile().list().length);
    for (int i = 1; i < 5; ++i) {
      File file = new File("./test_out/custom output/MyCustomOutput_" + i + ".txt");
      assertTrue(file.exists());
      file = new File("./test_out/custom output/MyCustomOutput2_" + i + ".txt");
      assertTrue(file.exists());
    }
  }

  @Test
  public void testAggregator() throws IOException, XMLStreamException, StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();
    
    DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator("ModelOutput.txt");
    finder.addPattern(creator.getParamMapPattern());
    finder.addPattern(creator.getFileSinkOutputPattern());
    
    DefaultOutputPatternCreator creator2 = new DefaultOutputPatternCreator("ModelOutput2.txt");
    finder.addPattern(creator2.getParamMapPattern());
    finder.addPattern(creator2.getFileSinkOutputPattern());
    
    List<MatchedFiles> files = finder.run(new File("./test_data/for_testing_simphony_model"));
    assertEquals(4, files.size());
    
    for (MatchedFiles mf : files) {
      mf.aggregateOutput("./test_out");
    }
    
    // create the expected output
    Set<String> expOut = new HashSet<String>();
    Set<String> expPOut = new HashSet<String>();
    for (File file : expectedFiles) {
      if (file.getName().contains(BatchConstants.PARAM_MAP_SUFFIX))
        getLines(expPOut, file.getPath());
      else
        getLines(expOut, file.getPath());
    }

    // read each line of the output and make sure it has
    // match with the expected.
    assertEquals(true, expOut.size() > 0);
    assertEquals(true, expPOut.size() > 0);
    
    File model = new File("./test_out/" + creator.getFileSinkOutputPattern().getPath());
    testOutput(expOut, model);
    File modelP = new File("./test_out/" + creator.getParamMapPattern().getPath());
    testOutput(expPOut, modelP);
    File model2 = new File("./test_out/" + creator2.getFileSinkOutputPattern().getPath());
    testOutput(expOut, model2);
    File modelP2 = new File("./test_out/" + creator2.getParamMapPattern().getPath());
    testOutput(expPOut, modelP2);
    
    model.delete();
    modelP.delete();
    model2.delete();
    modelP2.delete();
  }
  
  private Session getTestingRemote(Configuration config) {
    for (Session remote : config.sessions()) {
      if (remote.getUser().equals("sshtesting")) return remote;
    }
    return null;
  }

  @Test
  public void testRemoteStatus() throws StatusException, IOException  {
    File exp1 = new File("./test_out/sshtesting_128.135.250.205_1_failure.txt");
    if (exp1.exists()) exp1.delete();
    File exp2 = new File("./test_out/sshtesting_128.135.250.205_3_warn.txt");
    if (exp2.exists()) exp2.delete();
    
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    RemoteStatusGetter getter = new RemoteStatusGetter();
    RemoteSession remote = (RemoteSession) getTestingRemote(config);
    getter.run(remote, REMOTE_DIR);
    
    RemoteStatusCopier copier = new RemoteStatusCopier();
    copier.run(remote, REMOTE_DIR, "./test_out");
    
    assertEquals(true, exp1.exists());
    assertEquals(true, exp2.exists());
  }

  private void testOutput(Set<String> expected, File file) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(file));
      String line = null;
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.length() > 0) {
          assertEquals(line, true, expected.contains(line));
        }
      }
    } finally {
      if (reader != null)
        reader.close();
    }
  }
  
  @Test
  public void testRemoteStatusGetter()  throws IOException, StatusException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    RemoteStatusGetter getter = new RemoteStatusGetter();
    RemoteSession remote = (RemoteSession) getTestingRemote(config);
    getter.run(remote, REMOTE_DIR);
    
    assertEquals(remote.getStatus(1), RunningStatus.FAILURE);
    assertEquals(remote.getStatus(2), RunningStatus.OK);
    assertEquals(remote.getStatus(3), RunningStatus.WARN);
    assertEquals(remote.getStatus(4), RunningStatus.OK);
  }
}
