/**
 * 
 */
package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.OutputAggregator;
import repast.simphony.batch.ssh.RemoteOutputFinderCopier;
import repast.simphony.batch.ssh.RemoteSession;
import repast.simphony.batch.ssh.RemoteStatusCopier;
import repast.simphony.batch.ssh.StatusException;
import repast.simphony.batch.ssh.RemoteStatusGetter;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.Session;

/**
 * @author Nick Collier
 */
public class RemoteOutputTest {

  private static final String REMOTE_DIR = "for_testing_simphony_model";
  private static final String MODEL_OUT = "ModelOutput.2012.Aug.21.11_58_43.txt";
  private static final String P_OUT = "ModelOutput.2012.Aug.21.11_58_43.batch_param_map.txt";

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
  public void testAggregator() throws IOException {
    OutputAggregator aggregator = new OutputAggregator();
    aggregator.run(new ArrayList<File>(expectedFiles), "./test_out");

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
