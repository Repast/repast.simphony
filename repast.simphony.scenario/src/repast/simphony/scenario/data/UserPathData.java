package repast.simphony.scenario.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.util.ClassPathEntry;

/**
 * Encapsulates metadata about a model, the classpath etc. 
 * 
 * @author Nick Collier
 */
public class UserPathData {
  
  private String name;
  private List<ClassPathEntry> cpEntries = new ArrayList<ClassPathEntry>();
  private List<ClassPathEntry> annotationCPEntries = new ArrayList<ClassPathEntry>();
  private List<ClassPathEntry> agentCPEntries = new ArrayList<ClassPathEntry>();
  
  public UserPathData(String name) {
    this.name = name;
  }

  /**
   * Gets the name the model.
   * 
   * @return the name the model.
   */
  public String getName() {
    return name;
  }
  
  /**
   * Adds the specified entry to the classpath. If the entry is a directory and it
   * contains any .class files or its children of the directory contain
   * any .class path the directory itself will be added. If the directory
   * contains any jar files those will be added as well.<p>
   * 
   * If the entry ends with .jar that jar file will be added.
   * 
   * @param directory the directory to add
   * @param processAnnotations whether or not the entry should be processed for annotations
   *          
   */
  public void addEntry(String entry, boolean processAnnotations) {
    ClassPathEntry cpEntry = new ClassPathEntry(new File(entry));
    cpEntries.add(cpEntry);
    if (processAnnotations) annotationCPEntries.add(cpEntry);
  }
  
  /**
   * Adds the specified path as path on which agent classes can be found. 
   * 
   * @param path the path entry
   * @param filters
   */
  public void addAgentEntry(String path, List<String> filters) {
    ClassPathEntry cpEntry = new ClassPathEntry(new File(path), filters);
    agentCPEntries.add(cpEntry);
    cpEntries.add(cpEntry);
    annotationCPEntries.add(cpEntry);
  }
  
  /**
   * Gets the number of agent entries in this model data.
   * 
   * @return the number of agent entries in this model data.
   */
  public int getAgentEntryCount() {
    return agentCPEntries.size();
  }
  
  /**
   * Gets an iterable over the AgentPath entries in this model data.
   * 
   * @return an iterable over the AgentPath entries in this model data.
   */
  public Iterable<ClassPathEntry> agentEntries() {
    return agentCPEntries;
  }

  /**
   * Gets an iterable over all the classpath entries.
   * 
   * @return an iterable over all the classpath entries.
   */
  public Iterable<ClassPathEntry> classpathEntries() {
    return cpEntries;
  }
  
  /**
   * Gets an iterable over all the classpath entries that
   * should be processed for annotations.
   * 
   * @return an iterable over all the classpath entries that
   * should be processed for annotations.
   */
  public Iterable<ClassPathEntry> annotationCPEntries() {
    return annotationCPEntries;
  }
}
