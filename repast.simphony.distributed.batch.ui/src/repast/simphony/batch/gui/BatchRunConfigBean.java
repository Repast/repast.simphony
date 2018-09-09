package repast.simphony.batch.gui;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import repast.simphony.batch.gui.Host.Type;
import repast.simphony.batch.ssh.Configuration;
import repast.simphony.batch.ssh.OutputPattern;
import repast.simphony.batch.ssh.OutputPatternPropsParser;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

/**
 * Contains the data used by the GUI interface. 
 * 
 * @author Nick Collier
 */
public class BatchRunConfigBean {
  
  private static final String MODEL_DIRECTORY = "model.directory";
  private static final String SCENARIO_DIRECTORY = "scenario.directory";
  private static final String OUTPUT_DIRECTORY = "output.directory";
  private static final String KEY_DIRECTORY = "key.directory";
  private static final String BATCH_PARAM_FILE = "batch.param.file";
  private static final String PARAM_FILE = "param.file";
  private static final String POLL_FREQUENCY = "poll.frequency";
  private static final String HOST_PREFIX = "host";
  private static final String VM_ARGUMENTS = "vm.arguments";
  
  private static final String TYPE = "type";
  private static final String USER = "user";
  private static final String HOST_ADDRESS = "host_address";
  private static final String INSTANCES = "instances";
  private static final String KEY_FILE = "key_file";
  
  
  private String modelDirectory = "", scenarioDirectory = "", outputDirectory = "", 
      keyDirectory = System.getProperty("user.home") + "/.ssh",
      vmArguments = "";
  private String batchParameterFile = "", parameterFile = "";
  private double pollFrequency = 1;
  private List<Host> hosts = new ArrayList<Host>();
  private List<OutputPattern> patterns = new ArrayList<>();
  
  protected ExtendedPropertyChangeSupport pcs = new ExtendedPropertyChangeSupport(this);
  
  public BatchRunConfigBean() {
    Host host = new Host(Type.LOCAL);
    host.setInstances(2);
    hosts.add(host);
  }
  
  public BatchRunConfigBean(BatchRunConfigBean model) {
    this.modelDirectory = model.modelDirectory;
    this.scenarioDirectory = model.scenarioDirectory;
    this.outputDirectory = model.outputDirectory;
    this.keyDirectory = model.keyDirectory;
    this.vmArguments = model.vmArguments;
    this.batchParameterFile = model.batchParameterFile;
    this.parameterFile = model.parameterFile;
    this.pollFrequency = model.pollFrequency;
    for (Host host : model.hosts) {
      this.hosts.add(new Host(host));
    }
  }
  
  /**
   * @return the hosts
   */
  public List<Host> getHosts() {
    return new ArrayList<Host>(hosts);
  }

  /**
   * @param hosts the hosts to set
   */
  public void setHosts(List<Host> hosts) {
    this.hosts.clear();
    this.hosts.addAll(hosts);
  }

  /**
   * @return the modelDirectory
   */
  public String getModelDirectory() {
    return modelDirectory;
  }

  /**
   * @param modelDirectory the modelDirectory to set
   */
  public void setModelDirectory(String modelDirectory) {
    if (!this.modelDirectory.equals(modelDirectory)) {
      String tmp = this.modelDirectory;
      this.modelDirectory = modelDirectory;
      pcs.firePropertyChange("modelDirectory", tmp, this.modelDirectory);
    }
  }
  
  public List<OutputPattern> getOutputPatterns() {
    return patterns;
  }
  
  public void setOutputPatterns(List<OutputPattern> patterns) {
    this.patterns.clear();
    this.patterns.addAll(patterns);
    pcs.firePropertyChange("outputPatterns", null, this.patterns);
  }

  /**
   * @return the scenarioDirectory
   */
  public String getScenarioDirectory() {
    return scenarioDirectory;
  }

  /**
   * @param scenarioDirectory the scenarioDirectory to set
   */
  public void setScenarioDirectory(String scenarioDirectory) {
    if (!this.scenarioDirectory.equals(scenarioDirectory)) {
      String tmp = this.scenarioDirectory;
      this.scenarioDirectory = scenarioDirectory;
      pcs.firePropertyChange("scenarioDirectory", tmp, this.scenarioDirectory);
    }
  }

  /**
   * @return the outputDirectory
   */
  public String getOutputDirectory() {
    return outputDirectory;
  }

  /**
   * @param outputDirectory the outputDirectory to set
   */
  public void setOutputDirectory(String outputDirectory) {
    if (!this.outputDirectory.equals(outputDirectory)) {
      String tmp = this.outputDirectory;
      this.outputDirectory = outputDirectory;
      pcs.firePropertyChange("outputDirectory", tmp, this.outputDirectory);
    }
  }

  /**
   * @return the batchParameterFile
   */
  public String getBatchParameterFile() {
    return batchParameterFile;
  }

  /**
   * @param batchParameterFile the batchParameterFile to set
   */
  public void setBatchParameterFile(String batchParameterFile) {
    if (!this.batchParameterFile.equals(batchParameterFile)) {
      String tmp = this.batchParameterFile;
      this.batchParameterFile = batchParameterFile;
      pcs.firePropertyChange("batchParameterFile", tmp, this.batchParameterFile);
    }
  }

  /**
   * @return the keyDirectory
   */
  public String getKeyDirectory() {
    return keyDirectory;
  }

  /**
   * @param keyDirectory the keyDirectory to set
   */
  public void setKeyDirectory(String keyDirectory) {
    if (!this.keyDirectory.equals(keyDirectory)) {
      String tmp = this.keyDirectory;
      this.keyDirectory = keyDirectory;
      pcs.firePropertyChange("keyDirectory", tmp, this.keyDirectory);
    }
  }

  /**
   * @return the pollFrequency
   */
  public double getPollFrequency() {
    return pollFrequency;
  }

  /**
   * @param pollFrequency the pollFrequency to set
   */
  public void setPollFrequency(double pollFrequency) {
    if (this.pollFrequency != pollFrequency) {
      double tmp = this.pollFrequency;
      this.pollFrequency = pollFrequency;
      pcs.firePropertyChange("pollFrequency", tmp, this.pollFrequency);
    }
  }

  /**
   * @return the parameterFile
   */
  public String getParameterFile() {
    return parameterFile;
  }

  /**
   * @param parameterFile the parameterFile to set
   */
  public void setParameterFile(String parameterFile) {
    if (!this.parameterFile.equals(parameterFile)) {
      String tmp = this.parameterFile;
      this.parameterFile = parameterFile;
      pcs.firePropertyChange("parameterFile", tmp, this.parameterFile);
    }
  }
  
  public String getVMArguments() {
    return vmArguments;
  }

  public void setVMArguments(String vmArguments) {
    if (!this.vmArguments.equals(vmArguments)) {
      String tmp = this.vmArguments;
      this.vmArguments = vmArguments;
      pcs.firePropertyChange("VMArguments", tmp, this.vmArguments);
    }
  }

  public void load(File configFile) throws IOException {
    Properties props = new Properties();
    props.load(new FileReader(configFile));
    
    setModelDirectory(props.getProperty(MODEL_DIRECTORY, ""));
    setScenarioDirectory(props.getProperty(SCENARIO_DIRECTORY, ""));
    setOutputDirectory(props.getProperty(OUTPUT_DIRECTORY, ""));
    setKeyDirectory(props.getProperty(KEY_DIRECTORY, ""));
    setBatchParameterFile(props.getProperty(BATCH_PARAM_FILE, ""));
    setParameterFile(props.getProperty(PARAM_FILE, ""));
    setPollFrequency(Double.parseDouble(props.getProperty(POLL_FREQUENCY, "5")));
    setVMArguments(props.getProperty(VM_ARGUMENTS, ""));
    
    OutputPatternPropsParser parser = new OutputPatternPropsParser();
    setOutputPatterns(parser.parse(props));
    
    Map<Integer, Host> hostMap = new HashMap<Integer, Host>();
    for (String key : props.stringPropertyNames()) {
      if (key.startsWith(HOST_PREFIX)) {
        String[] vals = key.split("\\.");
        Integer id = Integer.valueOf(vals[1]);
        Host host = hostMap.get(id);
        if (host == null) {
          host = new Host(Type.LOCAL);
          hostMap.put(id, host);
        }
        
        if (vals[2].equals(TYPE)) host.setType(Type.valueOf(props.getProperty(key)));
        else if (vals[2].equals(INSTANCES)) host.setInstances(Integer.parseInt(props.getProperty(key)));
        else if (vals[2].equals(USER)) host.setUser(props.getProperty(key));
        else if (vals[2].equals(HOST_ADDRESS)) host.setHost(props.getProperty(key));
        else if (vals[2].equals(KEY_FILE)) host.setSSHKeyFile(props.getProperty(key));
      }
    }
    
    hosts.clear();
    hosts.addAll(hostMap.values());
    
  }
  
  /**
   * Saves the model as properties type file.
   * 
   * @param configFile
   * @throws IOException 
   */
  public void save(File configFile) throws IOException {
    Properties props = new Properties();
    props.setProperty(MODEL_DIRECTORY, getModelDirectory());
    props.setProperty(SCENARIO_DIRECTORY, getScenarioDirectory());
    props.setProperty(OUTPUT_DIRECTORY, getOutputDirectory());
    props.setProperty(KEY_DIRECTORY, getKeyDirectory());
    props.setProperty(BATCH_PARAM_FILE, getBatchParameterFile());
    props.setProperty(PARAM_FILE, getParameterFile());
    props.setProperty(POLL_FREQUENCY, String.valueOf(getPollFrequency()));
    props.setProperty(VM_ARGUMENTS, getVMArguments());
    
   
    int i = 1;
    for (OutputPattern pattern : patterns) {
      props.setProperty(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATH, pattern.getPath());
      props.setProperty(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.PATTERN, pattern.getPattern());
      props.setProperty(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.CONCATENATE, 
          String.valueOf(pattern.isConcatenate()));
      props.setProperty(Configuration.PATTERN_PREFIX + "." + i + "." + Configuration.HEADER, 
          String.valueOf(pattern.isHeader()));
      i++;
    }
    
    
    i = 0;
    for (Host host : hosts) {
      String prefix = HOST_PREFIX + "." + i + ".";
      props.setProperty(prefix + TYPE, host.getType().toString());
      props.setProperty(prefix + INSTANCES, String.valueOf(host.getInstances()));
      if (host.getType() == Host.Type.REMOTE) {
        props.setProperty(prefix + USER, host.getUser());
        props.setProperty(prefix + HOST_ADDRESS, host.getHost());
        props.setProperty(prefix + KEY_FILE, host.getSSHKeyFile());
      }
      i++;
    }
    
    
    
    props.store(new FileWriter(configFile), "");
  }
  
  public void addPropertyChangeListener(PropertyChangeListener x) {
    pcs.addPropertyChangeListener(x);
  }

  public void removePropertyChangeListener(PropertyChangeListener x) {
    pcs.removePropertyChangeListener(x);
  }
}