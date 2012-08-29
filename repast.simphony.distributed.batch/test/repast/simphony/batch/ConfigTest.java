package repast.simphony.batch;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import org.junit.Test;

import repast.simphony.batch.ssh.BatchParameterChunker;
import repast.simphony.batch.ssh.BatchParameterChunkerException;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.ModelArchiveConfigurator;
import repast.simphony.batch.ssh.ModelArchiveConfiguratorException;
import repast.simphony.batch.ssh.Remote;

public class ConfigTest {
  
  /*
   * model.archive = ./complete_model.zip
model.output = ./test_out
ssh.key_dir = ~/.ssh

remote.1.user = sshtesting
remote.1.host = 128.135.250.205
remote.1.instances = 4

remote.2.user = nick
remote.2.host = 192.168.1.12
remote.2.instances = 2

   */
  
  @Test
  public void testArchiveConfigurator() throws BatchParameterChunkerException, IOException, ModelArchiveConfiguratorException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();
    
    Iterator<Remote> iter = config.remotes().iterator();
    Remote r1 = iter.next();
    
    ModelArchiveConfigurator archConfig = new ModelArchiveConfigurator();
    File file = archConfig.configure(r1, config);
    System.out.println(file);
  }
  
  @Test
  public void testChunker() throws BatchParameterChunkerException, IOException {
    Configuration config = new Configuration("./test_data/test_remote_config.properties");
    BatchParameterChunker chunker = new BatchParameterChunker(config);
    chunker.run();
    
    Iterator<Remote> iter = config.remotes().iterator();
    Remote r1 = iter.next();
    Remote r2 = iter.next();
    
    String[] lines1 = r1.getInput().split("\n");
    String[] lines2 = r2.getInput().split("\n");
    
    if (r1.getUser().equals("nick")) {
      assertEquals(46, lines1.length);
      assertEquals(92, lines2.length);
    } else {
      assertEquals(46, lines2.length);
      assertEquals(92, lines1.length);
    }
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
    boolean oneFound = false, twoFound = false;
    for (Remote remote : config.remotes()) {
      if (remote.getUser().equals("sshtesting")) {
        assertEquals("128.135.250.205", remote.getHost());
        assertEquals(4, remote.getInstances());
        oneFound = true;
      } else {
        assertEquals("nick", remote.getUser());
        assertEquals("192.168.1.12", remote.getHost());
        assertEquals(2, remote.getInstances());
        twoFound = true;
      }
      count++;
    }
    
    assertEquals(2, count);
    assertEquals(true, oneFound);
    assertEquals(true, twoFound);
  }

}
