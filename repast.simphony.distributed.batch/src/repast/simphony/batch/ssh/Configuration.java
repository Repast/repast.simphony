/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Configuration data loaded from a config file for configuring remote ssh based execution. 
 * 
 * @author Nick Collier
 */
public class Configuration {
  
  private static final String MA_KEY = "model.archive";
  private static final String SSH_DIR_KEY = "ssh.key_dir";
  private static final String OUT_DIR_KEY = "model.output";
  private static final String BATCH_PARAMS_KEY = "batch.params.file";
  
  private String modelArchive, sshKeyDir, outDir, paramsFile;
  private List<Remote> remotes;
  
  public Configuration(String file) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(file));
    
    modelArchive = props.getProperty(MA_KEY);
    if (modelArchive == null) throw new IOException("Invalid configuration file: file is missing " + MA_KEY + " property");
    props.remove(MA_KEY);
    
    sshKeyDir = props.getProperty(SSH_DIR_KEY);
    if (sshKeyDir == null) throw new IOException("Invalid configuration file: file is missing " + SSH_DIR_KEY + " property");
    sshKeyDir = sshKeyDir.trim();
    if (sshKeyDir.contains("~")) sshKeyDir = sshKeyDir.replace("~", System.getProperty("user.home"));
    props.remove(SSH_DIR_KEY);
    
    outDir = props.getProperty(OUT_DIR_KEY);
    if (outDir == null) throw new IOException("Invalid configuration file: file is missing " + OUT_DIR_KEY + " property");
    props.remove(OUT_DIR_KEY);
    
    paramsFile = props.getProperty(BATCH_PARAMS_KEY);
    if (paramsFile == null) throw new IOException("Invalid configuration file: file is missing " + BATCH_PARAMS_KEY + " property");
    props.remove(BATCH_PARAMS_KEY);
    
    remotes = new RemotePropsParser().parse(props);
  }
  
  public String getModelArchive() {
    return modelArchive;
  }
  
  public String getBatchParamsFile() {
    return paramsFile;
  }
  
  public String getSSHKeyDir() {
    return sshKeyDir;
  }
  
  public String getOutputDir() {
    return outDir;
  }
  
  /**
   * Gets the numer of specified remote locations to run the model.
   * 
   * @return the numer of specified remote locations to run the model.
   */
  public int getRemoteCount() {
    return remotes.size();
  }
  
  /**
   * Gets an iterable over the Remotes described in this Configuration.
   * 
   * @return  an iterable over the Remotes described in this Configuration.
   */
  public Iterable<Remote> remotes() {
    return remotes;
  }

}
