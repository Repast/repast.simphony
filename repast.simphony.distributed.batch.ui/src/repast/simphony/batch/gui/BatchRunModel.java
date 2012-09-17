package repast.simphony.batch.gui;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the data used by the GUI interface. 
 * 
 * @author Nick Collier
 */
public class BatchRunModel {
  
  private String modelDirectory, scenarioDirectory, outputDirectory;
  private String batchParameterFile;
  private List<Host> hosts = new ArrayList<Host>();
  
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
    this.modelDirectory = modelDirectory;
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
    this.scenarioDirectory = scenarioDirectory;
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
    this.outputDirectory = outputDirectory;
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
    this.batchParameterFile = batchParameterFile;
  }
  
}