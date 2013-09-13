package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;

import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.RemoteOutputFinderCopier;
import repast.simphony.batch.ssh.RemoteSession;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.Session;
import repast.simphony.batch.ssh.StatusException;

public class FindOutputTests {

  static {
    PropertyConfigurator.configure("./config/SSH.MessageCenter.log4j.properties");
    String home = System.getProperty("user.home");
    SSHSessionFactory.init(home + "/.ssh");
  }

  private static final String[] EXP = { "instance_1/one", "instance_1/one_batch_param_map",
      "instance_1/output/two.txt", "instance_1/output/two_batch_param_map.txt", "instance_2/one",
      "instance_2/one_batch_param_map", "instance_2/output/two.txt",
      "instance_2/output/two_batch_param_map.txt" };

  private static final String[] EXP2 = { "instance_1/one", "instance_1/one_batch_param_map",
      "instance_1/output/two.txt", "instance_1/output/two_batch_param_map.txt", "instance_2/one",
      "instance_2/one_batch_param_map", "instance_2/output/two.txt",
      "instance_2/output/two_batch_param_map.txt",
      "instance_1/output/more_output/some_output123.txt",
      "instance_2/output/more_output/some_output345.txt" };

  private Set<String> createSet(String prefix, String[] expected) {
    Set<String> set = new HashSet<>();
    File file = new File(prefix);
    for (String f : expected) {
      set.add(new File(file, f).getAbsolutePath().replace("\\", "/"));
    }
    return set;
  }

  @Test
  public void matchTests() {
    // paths don't have to exist
    PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/foo*.txt");
    assertTrue(matcher.matches(new File("a/b/c/foo_abc.txt").toPath()));
    assertTrue(!matcher.matches(new File("a/b/c/abc.txt").toPath()));
  }

  @Test
  public void findViaBatchParam() throws StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();
    List<File> files = finder.run(new File("./test_data"));
    assertEquals(8, files.size());
    Set<String> set = createSet("./test_data", EXP);

    for (File file : files) {
    	String f = file.getAbsolutePath().replace("\\", "/");
        assertTrue(f, set.remove(f));
    }
    assertEquals(0, set.size());
  }

  @Test
  public void testPattern() throws StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();
    finder.addPattern("**/more_output/some_output*.txt");
    List<File> files = finder.run(new File("./test_data"));
    assertEquals(10, files.size());
    Set<String> set = createSet("./test_data", EXP2);

    for (File file : files) {
    	String f = file.getAbsolutePath().replace("\\", "/");
      assertTrue(f, set.remove(f));
    }
    assertEquals(0, set.size());
  }

  private Session getTestingRemote(Configuration config) {
    for (Session remote : config.sessions()) {
      if (remote.getUser().equals("sshtesting"))
        return remote;
    }
    return null;
  }

  @Test
  public void testRemoteFinder() throws IOException, StatusException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    RemoteSession remote = (RemoteSession) getTestingRemote(config);
    RemoteOutputFinderCopier finder = new RemoteOutputFinderCopier();
    finder.addPattern("**/more_output/some_output*.txt");
    
    Path local = Files.createTempDirectory(String.valueOf(System.currentTimeMillis()));
    List<File> out = finder.run(remote, "for_testing_simphony_model2", local.toString());
    assertEquals(10, out.size());
    Set<String> set = createSet(local.toString() + "/for_testing_simphony_model2", EXP2);

    for (File file : out) {
    	String f = file.getAbsolutePath().replace("\\", "/");
        assertTrue(f, set.remove(f));
    }
    assertEquals(0, set.size());
  }
}
