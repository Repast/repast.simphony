package repast.simphony.batch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import repast.simphony.batch.ssh.BatchParameterChunker;
import repast.simphony.batch.ssh.BatchParameterChunkerException;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.ModelArchiveConfigurator;
import repast.simphony.batch.ssh.ModelArchiveConfiguratorException;
import repast.simphony.batch.ssh.Session;

public class ConfigTest {
  
  private static String[] EXPECTED_1 = {
    "1\trandomSeed\t1,human_count\t100,zombie_count\t1",
    "2\trandomSeed\t1,human_count\t100,zombie_count\t2",
    "3\trandomSeed\t1,human_count\t100,zombie_count\t3",
    "4\trandomSeed\t1,human_count\t100,zombie_count\t4",
    "5\trandomSeed\t1,human_count\t100,zombie_count\t5",
    "6\trandomSeed\t1,human_count\t100,zombie_count\t6",
    "7\trandomSeed\t1,human_count\t100,zombie_count\t7",
    "8\trandomSeed\t1,human_count\t100,zombie_count\t8",
    "9\trandomSeed\t1,human_count\t100,zombie_count\t9",
    "10\trandomSeed\t1,human_count\t100,zombie_count\t10",
    "11\trandomSeed\t1,human_count\t100,zombie_count\t11",
    "12\trandomSeed\t1,human_count\t120,zombie_count\t1"};
  
  private static String[] EXPECTED_2 = {
    "13\trandomSeed\t1,human_count\t120,zombie_count\t2",
    "14\trandomSeed\t1,human_count\t120,zombie_count\t3",
    "15\trandomSeed\t1,human_count\t120,zombie_count\t4",
    "16\trandomSeed\t1,human_count\t120,zombie_count\t5"};
  
  private static String[] EXPECTED_3 = {
    "17\trandomSeed\t1,human_count\t120,zombie_count\t6",
    "18\trandomSeed\t1,human_count\t120,zombie_count\t7",
    "19\trandomSeed\t1,human_count\t120,zombie_count\t8",
    "20\trandomSeed\t1,human_count\t120,zombie_count\t9",
    "21\trandomSeed\t1,human_count\t120,zombie_count\t10",
    "22\trandomSeed\t1,human_count\t120,zombie_count\t11"};

  @Test
  public void testArchiveConfigurator() throws BatchParameterChunkerException, IOException,
      ModelArchiveConfiguratorException {
    Configuration config = new Configuration("./test_data/test_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();

    Iterator<? extends Session> iter = config.sessions().iterator();
    Session r1 = iter.next();

    ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
    System.out.println(archConfig.configure(r1, config));
  }
  
  private List<String> getFileContents(String file) {
    List<String> contents = new ArrayList<>();
    String line = null;
    try (BufferedReader reader = new BufferedReader(new FileReader(new File(file)))) {
      while ((line = reader.readLine()) != null) {
        line = line.trim();
        contents.add(line);
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      assertTrue(false);
    } catch (IOException e) {
      e.printStackTrace();
      assertTrue(false);
    }
    
    return contents;
  }

  @Test
  public void testChunker() throws BatchParameterChunkerException, IOException {
    Configuration config = new Configuration("./test_data/test_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();

    Session session1 = findSession(config, "sshtesting", "128.135.250.205");
    assertNotNull(session1);
    Session session2 = findSession(config, "nick", "192.168.1.12");
    assertNotNull(session2);
    
    Session session3 = findSession(config, System.getProperty("user.name"), "localhost");
    assertNotNull(session3);
    
    List<String> contents = getFileContents(session1.getInput());
    assertEquals(EXPECTED_1.length, contents.size());
    for (int i = 0; i < contents.size(); ++i) {
      assertEquals(EXPECTED_1[i], contents.get(i));
    }
    
    contents = getFileContents(session2.getInput());
    assertEquals(EXPECTED_2.length, contents.size());
    for (int i = 0; i < contents.size(); ++i) {
      assertEquals(EXPECTED_2[i], contents.get(i));
    }
    
    contents = getFileContents(session3.getInput());
    assertEquals(EXPECTED_3.length, contents.size());
    for (int i = 0; i < contents.size(); ++i) {
      assertEquals(EXPECTED_3[i], contents.get(i));
    }
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
    Configuration config = new Configuration("./test_data/test_config.properties");
    assertEquals("./test_data/complete_model.jar", config.getModelArchive());
    assertEquals("./output", config.getOutputDir());
    String keydir = "/Users/nick/" + ".ssh";
    assertEquals(keydir, config.getSSHKeyDir());
    assertEquals(6.0f, config.getPollFrequency(), 0);
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
