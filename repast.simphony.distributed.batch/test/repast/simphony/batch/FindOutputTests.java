package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import repast.simphony.batch.ssh.DefaultOutputPatternCreator;
import repast.simphony.batch.ssh.LocalOutputFinder;
import repast.simphony.batch.ssh.MatchedFiles;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.RemoteOutputFinderCopier;
import repast.simphony.batch.ssh.RemoteSession;
import repast.simphony.batch.ssh.SSHSessionFactory;
import repast.simphony.batch.ssh.Session;
import repast.simphony.batch.ssh.StatusException;
import simphony.util.messages.MessageCenter;

public class FindOutputTests {

  static {
      
    try {
        PropertyConfigurator.configure(MessageCenter.updateProperties("./config/SSH.MessageCenter.log4j.properties"));
    } catch (IOException e) {
        e.printStackTrace();
    }
    String home = System.getProperty("user.home");
    SSHSessionFactory.init(home + "/.ssh");
  }

  private static final String[] ONE_EXP = { "instance_1/one", "instance_2/one" };
  private static final String[] TWO_EXP = { "instance_1/output/two.txt",
      "instance_2/output/two.txt" };
  private static final String[] ONE_BP_EXP = { "instance_1/one.batch_param_map",
      "instance_2/one.batch_param_map" };
  private static final String[] TWO_BP_EXP = { "instance_1/output/two.batch_param_map.txt",
      "instance_2/output/two.batch_param_map.txt" };
  private static final String[] OTHER_EXP = { "instance_1/output/more_output/some_output123.txt",
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

  private void testFoundFiles(String msg, MatchedFiles mf, String prefix, String[] exp) {
    Set<String> set = createSet(prefix, exp);
    for (File file : mf.getFiles()) {
      String f = file.getAbsolutePath().replace("\\", "/");
      assertTrue(msg + ": " + f, set.remove(f));
    }
    assertEquals(0, set.size());
  }

  @Test
  public void findViaBatchParam() throws StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();

    DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator("one", true);
    OutputPattern onePattern = creator.getFileSinkOutputPattern();
    OutputPattern oneBPPattern = creator.getParamMapPattern();
    finder.addPattern(oneBPPattern);
    finder.addPattern(onePattern);

    creator = new DefaultOutputPatternCreator("two.txt", true);
    OutputPattern twoPattern = creator.getFileSinkOutputPattern();
    OutputPattern twoBPPattern = creator.getParamMapPattern();
    finder.addPattern(twoBPPattern);
    finder.addPattern(twoPattern);

    List<MatchedFiles> files = finder.run(new File("./test_data"));
    assertEquals(4, files.size());

    boolean[] found = new boolean[4];
    for (MatchedFiles mf : files) {
      if (mf.getPattern().equals(onePattern)) {
        testFoundFiles("one", mf, "./test_data", ONE_EXP);
        found[0] = true;
      } else if (mf.getPattern().equals(twoPattern)) {
        testFoundFiles("two", mf, "./test_data", TWO_EXP);
        found[1] = true;
      } else if (mf.getPattern().equals(oneBPPattern)) {
        testFoundFiles("one bp", mf, "./test_data", ONE_BP_EXP);
        found[2] = true;
      } else if (mf.getPattern().equals(twoBPPattern)) {
        testFoundFiles("two bp", mf, "./test_data", TWO_BP_EXP);
        found[3] = true;
      } else {
        fail("bad pattern");
      }
    }

    for (int i = 0; i < found.length; i++) {
      assertTrue("matched files " + i + "not tested", found[i]);
    }
  }

  @Test
  public void testPattern() throws StatusException {
    LocalOutputFinder finder = new LocalOutputFinder();

    DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator("one", true);
    OutputPattern onePattern = creator.getFileSinkOutputPattern();
    OutputPattern oneBPPattern = creator.getParamMapPattern();
    finder.addPattern(oneBPPattern);
    finder.addPattern(onePattern);

    creator = new DefaultOutputPatternCreator("two.txt", true);
    OutputPattern twoPattern = creator.getFileSinkOutputPattern();
    OutputPattern twoBPPattern = creator.getParamMapPattern();
    finder.addPattern(twoBPPattern);
    finder.addPattern(twoPattern);

    OutputPattern otherPattern = new OutputPattern();
    otherPattern.setPattern("**/more_output/some_output*.txt");
    otherPattern.setPath("some_output.txt");
    finder.addPattern(otherPattern);

    List<MatchedFiles> files = finder.run(new File("./test_data"));
    assertEquals(5, files.size());

    boolean[] found = new boolean[5];
    for (MatchedFiles mf : files) {
      if (mf.getPattern().equals(onePattern)) {
        testFoundFiles("one", mf, "./test_data", ONE_EXP);
        found[0] = true;
      } else if (mf.getPattern().equals(twoPattern)) {
        testFoundFiles("two", mf, "./test_data", TWO_EXP);
        found[1] = true;
      } else if (mf.getPattern().equals(oneBPPattern)) {
        testFoundFiles("one bp", mf, "./test_data", ONE_BP_EXP);
        found[2] = true;
      } else if (mf.getPattern().equals(twoBPPattern)) {
        testFoundFiles("two bp", mf, "./test_data", TWO_BP_EXP);
        found[3] = true;
      } else if (mf.getPattern().equals(otherPattern)) {
        testFoundFiles("other", mf, "./test_data", OTHER_EXP);
        found[4] = true;
      } else {
        fail("bad pattern");
      }
    }

    for (int i = 0; i < found.length; i++) {
      assertTrue("matched files " + i + "not tested", found[i]);
    }
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
    
    DefaultOutputPatternCreator creator = new DefaultOutputPatternCreator("one", true);
    OutputPattern onePattern = creator.getFileSinkOutputPattern();
    OutputPattern oneBPPattern = creator.getParamMapPattern();
    finder.addPattern(oneBPPattern);
    finder.addPattern(onePattern);

    creator = new DefaultOutputPatternCreator("two.txt", true);
    OutputPattern twoPattern = creator.getFileSinkOutputPattern();
    OutputPattern twoBPPattern = creator.getParamMapPattern();
    finder.addPattern(twoBPPattern);
    finder.addPattern(twoPattern);

    OutputPattern otherPattern = new OutputPattern();
    otherPattern.setPattern("**/more_output/some_output*.txt");
    otherPattern.setPath("some_output.txt");
    finder.addPattern(otherPattern);

    Path local = Files.createTempDirectory(String.valueOf(System.currentTimeMillis()));
    List<MatchedFiles> files = finder.run(remote, "for_testing_simphony_model2", local.toString());
    assertEquals(5, files.size());

    String prefix = local.toString() + "/for_testing_simphony_model2";
    boolean[] found = new boolean[5];
    for (MatchedFiles mf : files) {
      if (mf.getPattern().equals(onePattern)) {
        testFoundFiles("one", mf, prefix, ONE_EXP);
        found[0] = true;
      } else if (mf.getPattern().equals(twoPattern))  {
        testFoundFiles("two", mf, prefix, TWO_EXP);
        found[1] = true;
      } else if (mf.getPattern().equals(oneBPPattern))  {
        testFoundFiles("one bp", mf, prefix, ONE_BP_EXP);
        found[2] = true;
      } else if (mf.getPattern().equals(twoBPPattern)) {
        testFoundFiles("two bp", mf, prefix, TWO_BP_EXP);
        found[3] = true;
      } else if (mf.getPattern().equals(otherPattern))  {
        testFoundFiles("other", mf, prefix, OTHER_EXP);
        found[4] = true;
      } else {
        fail("bad pattern");
      }
    }

    for (int i = 0; i < found.length; i++) {
      assertTrue("matched files " + i + "not tested", found[i]);
    }
  }
}
