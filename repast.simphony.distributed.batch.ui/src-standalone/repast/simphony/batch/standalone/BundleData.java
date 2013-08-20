/**
 * 
 */
package repast.simphony.batch.standalone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates data about a Bundle read from a Manifest.MF
 * @author Nick Collier
 */
public class BundleData {
  
  private String name;
  private Version version;
  private List<String> classpath = new ArrayList<String>();
  private List<BundleData> requiredBundles = new ArrayList<BundleData>();
  private File location;
  
  public BundleData(String name, Version version) {
    this.name = name;
    this.version = version;
  }
  
  public void setLocation(File location) {
    this.location = location;
  }
  
  public File getLocation() {
    return location;
  }
  
  /**
   * Adds the specified BundleData as a required Bundle.
   * 
   * @param data
   */
  public void addRequiredBundle(BundleData data) {
    requiredBundles.add(data);
  }
  
  /**
   * Gets an iterable over the bundles required by this bundle.
   * 
   * @return an iterable over the bundles required by this bundle.
   */
  public Iterable<BundleData> requiredBundles() {
    return requiredBundles;
  }
  
  /**
   * Adds the specified string to the classpath for this bundle.
   * 
   * @param path
   */
  public void addToClassPath(String path) {
    classpath.add(path);
  }
  
  /**
   * Gets an iterable over all the entries in this BundleData's classpath.
   * 
   * @return  an iterable over all the entries in this BundleData's classpath.
   */
  public Iterable<String> classPathEntries() {
    return classpath;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @return the version
   */
  public Version getVersion() {
    return version;
  }
}
