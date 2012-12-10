package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import repast.simphony.batch.ssh.BatchParameterChunker;
import repast.simphony.batch.ssh.BatchParameterChunkerException;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.ModelArchiveConfigurator;
import repast.simphony.batch.ssh.ModelArchiveConfiguratorException;
import repast.simphony.batch.ssh.Session;

public class ConfigTest {

  /*
   * model.archive = ./complete_model.zip model.output = ./test_out ssh.key_dir
   * = ~/.ssh
   * 
   * remote.1.user = sshtesting remote.1.host = 128.135.250.205
   * remote.1.instances = 4
   * 
   * remote.2.user = nick remote.2.host = 192.168.1.12 remote.2.instances = 2
   */

  @Test
  public void testArchiveConfigurator() throws BatchParameterChunkerException, IOException,
      ModelArchiveConfiguratorException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();

    Iterator<? extends Session> iter = config.sessions().iterator();
    Session r1 = iter.next();

    ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
    archConfig.configure(r1, config);
    // System.out.println(file);
  }

  @Test
  public void testChunker() throws BatchParameterChunkerException, IOException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();

    Session session1 = findSession(config, "sshtesting", "128.135.250.205");
    assertNotNull(session1);
    Session session2 = findSession(config, "nick", "192.168.1.12");
    assertNotNull(session2);
    
    String user = System.getProperty("user.name");
    Session session3 = findSession(config, user, "localhost");
    assertNotNull(session3);

    String[] lines1 = session1.getInput().split("\n");
    String[] lines2 = session2.getInput().split("\n");
    String[] lines3 = session3.getInput().split("\n");
    
    // total number of lines should be 138
    assertEquals(138, lines1.length + lines2.length + lines3.length);
    assertEquals(63, lines1.length);
    assertEquals(30, lines2.length);
    assertEquals(45, lines3.length);
  }

  public Session findSession(Configuration config, String user, String host) {
    for (Session session : config.sessions()) {
      if (session.getUser().equals(user) && session.getHost().equals(host))
        return session;
    }
    return null;

  }

  @Test
  public void testConfig() throws IOException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    assertEquals("./test_data/complete_model.zip", config.getModelArchive());
    assertEquals("./test_out", config.getOutputDir());
    String keydir = System.getProperty("user.home") + "/" + ".ssh";
    assertEquals(keydir, config.getSSHKeyDir());
    assertEquals(10f, config.getPollFrequency(), 0);
    assertEquals("scenario.rs/batch_params.xml", config.getBatchParamsFile());

    int count = 0;

    for (Session session : config.sessions()) {
      count++;
    }
    assertEquals(3, count);

    Session session = findSession(config, "sshtesting", "128.135.250.205");
    assertNotNull(session);
    assertEquals("128.135.250.205", session.getHost());
    assertEquals("sshtesting", session.getUser());
    assertEquals(4, session.getInstances());

    session = findSession(config, "nick", "192.168.1.12");
    assertNotNull(session);
    assertEquals("nick", session.getUser());
    assertEquals("192.168.1.12", session.getHost());
    assertEquals(2, session.getInstances());
    
    String user = System.getProperty("user.name");
    session = findSession(config, user, "localhost");
    assertNotNull(session);
    assertEquals(user, session.getUser());
    assertEquals("localhost", session.getHost());
    assertEquals(3, session.getInstances());
  }

}
