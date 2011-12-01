/**
 * 
 */
package repast.simphony.scenario.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.util.ClassFinder;
import repast.simphony.util.ClassPathEntry;

/**
 * Encapsulates a classpath.
 * 
 * @author Nick Collier
 */
public class Classpath {

  private List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();

  /**
   * Adds the specified entry to the classpath. If the entry is a directory and
   * it contains any .class files or its children of the directory contain any
   * .class path the directory itself will be added. If the directory contains
   * any jar files those will be added as well.
   * <p>
   * 
   * If the entry ends with .jar that jar file will be added.
   * 
   * @param entry
   *          the entry to add
   * @return true if classes or jars were found and the entry was added,
   *         otherwise false.
   */
  public boolean addEntry(String entry) {
    File file = new File(entry);
    return addEntry(new ClassPathEntry(file));
  }
  
  /**
   * Adds the specified entry to the classpath. If the entry is a directory and
   * it contains any .class files or its children of the directory contain any
   * .class path the directory itself will be added. If the directory contains
   * any jar files those will be added as well.
   * <p>
   * 
   * If the entry ends with .jar that jar file will be added.
   * 
   * @param entry
   *          the entry to add
   * @return true if classes or jars were found and the entry was added,
   *         otherwise false.
   */
  public boolean addEntry(ClassPathEntry entry) {
    entries.add(entry);
    return entry.isValid();
  }

  /**
   * Gets the classes in the currently added entries.
   * 
   * @return the classes in the currently added entries.
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public List<Class<?>> getClasses() throws IOException, ClassNotFoundException {
    ClassFinder finder = new ClassFinder();
    for (ClassPathEntry entry : entries) {
      finder.addEntry(entry);
    }
    return finder.findClasses();
  }

  
  /**
   * Gets an iterable over all the classpath entries.
   * 
   * @return an iterable over all the classpath entries.
   */
  public Iterable<ClassPathEntry> entries() {
    return entries;
  }
  
  /**
   * Gets an iterable over all the possible paths on which
   * classes may be found. These include top level directories
   * and jar files.
   * 
   * @return an iterable over all the possible paths on which
   * classes may be found. These include top level directories
   * and jar files.
   */
  public Iterable<File> classPaths() {
    Set<File> paths = new HashSet<File>();
    for (ClassPathEntry entry : entries) {
      paths.addAll(entry.getClassPaths());
    }
    return paths;
  }
}
