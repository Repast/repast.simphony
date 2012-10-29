/**
 * 
 */
package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
import repast.simphony.batch.ssh.OutputAggregator;
import repast.simphony.batch.ssh.RemoteOutputFinderCopier;
import repast.simphony.batch.ssh.RemoteSession;
import repast.simphony.batch.ssh.RemoteStatusCopier;
import repast.simphony.batch.ssh.StatusException;
import repast.simphony.batch.ssh.RemoteStatusGetter;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.Session;
import repast.simphony.data2.engine.FileSinkControllerActionIO;

/**
 * @author Nick Collier
 */
public class RemoteOutputTest {

  private static final String REMOTE_DIR = "for_testing_simphony_model";
  private static final String MODEL_OUT = "ModelOutput.2012.Aug.21.11_58_43.txt";
  private static final String MODEL2_OUT = "ModelOutput.2012.Aug.21.11_58_43.txt";
  private static final String P_OUT = "ModelOutput.2012.Aug.21.11_58_43.batch_param_map.txt";
  private static final String P2_OUT = "ModelOutput.2012.Aug.21.11_58_43.batch_param_map.txt";

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
    assertEquals(1, baseNames.size());
    assertEquals("ModelOutput.txt", baseNames.get(0));
  }

  @Test
  public void testAggregator() throws IOException, XMLStreamException {
    OutputAggregator aggregator = new OutputAggregator();
    aggregator.run(new BaseOutputNamesFinder().find("./test_data/test_scenario.rs"), 
        new ArrayList<File>(expectedFiles), "./test_out");

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
    testOutput(expOut, new File("./test_out/" + MODEL_OUT));
    testOutput(expPOut, new File("./test_out/" + P_OUT));
    testOutput(expOut, new File("./test_out/" + MODEL2_OUT));
    testOutput(expPOut, new File("./test_out/" + P2_OUT));
  }
  
  private Session getTestingRemote(Configuration config) {
    for (Session remote : config.sessions()) {
      if (remote.getUser().equals("sshtesting")) return remote;
    }
    return null;
  }

  @Test
  public void testRemoteStatus() throws StatusException, IOException  {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    RemoteStatusGetter getter = new RemoteStatusGetter();
    RemoteSession remote = (RemoteSession) getTestingRemote(config);
    getter.run(remote, REMOTE_DIR);
    
    RemoteStatusCopier copier = new RemoteStatusCopier();
    copier.run(remote, REMOTE_DIR, "./test_out");
    
    assertEquals(true, new File("./test_out/sshtesting_128.135.250.205_1_failure.txt").exists());
    assertEquals(true, new File("./test_out/sshtesting_128.135.250.205_3_warn.txt").exists());
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
  public void testRemoteCopy() throws IOException, StatusException  {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    // Session session = SSHUtil.connect(remotes.get(0));
    // SSHUtil.copyFileToRemote(session, remotes.get(0).getModelArchive());
    RemoteOutputFinderCopier copier = new RemoteOutputFinderCopier();
    RemoteSession remote = (RemoteSession) getTestingRemote(config);
    List<File> copiedFiles = copier.run(remote, REMOTE_DIR, "./test_out");
    
    // 4 outputs from 4 instances
    assertEquals(16, copiedFiles.size());

    for (File file : copiedFiles) {
      File match = findExpectedMatch(file);
      assertNotNull(match);
      assertEquals(match.length(), file.length());
      expectedFiles.remove(match);
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

  private File findExpectedMatch(File file) {
    String path = file.getPath();
    path = path.replace("./test_out", "./test_data");

    for (File exp : expectedFiles) {
      if (path.equals(exp.getPath()))
        return exp;
    }

    return null;

  }

}
