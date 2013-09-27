/**
 * 
 */
package repast.simphony.batch.ssh;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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
  private static final String POLL_INTERVAL_KEY = "poll.frequency";
  private static final String VM_ARGS_KEY = "vm.arguments";
  private static final String PATTERN_PREFIX = "output.pattern";
  
  private String modelArchive, sshKeyDir, outDir, paramsFile, vmArgs;
  private float pollFrequency;
  private List<? extends Session> sessions;
  private List<String> patterns = new ArrayList<>();
  
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
    
    String sPoll = props.getProperty(POLL_INTERVAL_KEY);
    if (sPoll == null) throw new IOException("Invalid configuration file: file is missing " + POLL_INTERVAL_KEY + " property");
    try {
      pollFrequency = Float.parseFloat(sPoll);
    } catch (NumberFormatException ex) {
      throw new IOException("Invalid configuration file: " + POLL_INTERVAL_KEY + " property must be a number");
    }
    props.remove(POLL_INTERVAL_KEY);
    
    vmArgs = props.getProperty(VM_ARGS_KEY);
    if (vmArgs == null) throw new IOException("Invalid configuration file: file is missing " + VM_ARGS_KEY + " property");
    props.remove(VM_ARGS_KEY);
    
    // order is important here as SessionPropsParser assumes
    // that all the non-session properties have been removed from
    // the properties file
    parseOutputPatterns(props);
    sessions = new SessionPropsParser().parse(props);
  }
  
  private void parseOutputPatterns(Properties props) throws IOException {
    List<String> toRemove = new ArrayList<>();
    for (String key : props.stringPropertyNames()) {
      if (key.startsWith(PATTERN_PREFIX)) {
        String pattern = props.getProperty(key).trim();
        if (pattern.trim().length() == 0) {
          throw new IOException("Invalid configuration file: " + key + " is missing pattern value.");
        }
        toRemove.add(key);
        patterns.add(pattern);
      }
    }
    
    for (String key : toRemove) {
      props.remove(key);
    }
  }
  
  /**
   * Gets the output patterns for this configuration. The output patterns
   * should be specified in "glob" format using only "/".
   * 
   * @return the output patterns for this configuration.
   */
  public List<String> getOutputPatterns() {
    return new ArrayList<String>(patterns);
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
   * Gets how often, in seconds, to poll remotes to see if they are done.
   * @return
   */
  public float getPollFrequency() {
    return pollFrequency;
  }
  
  /**
   * Gets the numer of specified remote locations to run the model.
   * 
   * @return the numer of specified remote locations to run the model.
   */
  public int getRemoteCount() {
    return sessions.size();
  }
  
  /**
   * Gets any arguments to pass the VM that runs the model.
   * 
   * @return any arguments to pass the VM that runs the model.
   */
  public String getVMArguments() {
    return vmArgs;
  }
  
  /**
   * Gets an iterable over the Remotes described in this Configuration.
   * 
   * @return  an iterable over the Remotes described in this Configuration.
   */
  public Iterable<? extends Session> sessions() {
    return sessions;
  }

}
