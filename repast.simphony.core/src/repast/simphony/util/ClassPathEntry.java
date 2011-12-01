/**
 * 
 */
package repast.simphony.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import repast.simphony.filter.Filter;
import repast.simphony.filter.OrFilter;

/**
 * Encapsulates a path on which classes or jars might be found together with a
 * class name filters. The filters work according to "or" semantics, so if any
 * filter is passed then the class name passes.
 * 
 * @author Nick Collier
 */
public class ClassPathEntry {

  private static Filter<String> ALWAYS_TRUE = new Filter<String>() {
    public boolean evaluate(String object) {
      return true;
    }
  };

  private OrFilter<String> filter = new OrFilter<String>();
  private File path;

  public ClassPathEntry(File path) {
    this.path = path;
  }

  public ClassPathEntry(File path, ClassPathFilter filter) {
    this.path = path;
    this.filter.addFilter(filter);
  }
  
  public ClassPathEntry(File path, List<String> filters) {
    this.path = path;
    for (String f : filters) {
      addFilter(f);
    }
  }

  /**
   * Adds the specified filter to this PathEntry.
   * 
   * @param filter
   *          the filter to add
   */
  public void addFilter(ClassPathFilter filter) {
    this.filter.addFilter(filter);
  }

  /**
   * Adds the specified filter as a ClassPathFilter to this PathEntry.
   * 
   * @param filter
   *          the filter to add
   */
  public void addFilter(String filter) {
    if (filter.equals("*")) {
      this.filter.addFilter(ALWAYS_TRUE);
    } else {
      this.filter.addFilter(new ClassPathFilter(filter));
    }
  }

  /**
   * Gets the path.
   * 
   * @return the path.
   */
  public File getPath() {
    return path;
  }

  /**
   * Returns true if the
   * 
   * @param className
   * @return
   */
  public boolean filter(String className) {
    return filter.evaluate(className);
  }
  
  /**
   * Gets whether or not this ClassPathEntry contains a directory that contains classes, or jars.
   * @return
   */
  public boolean isValid() {
    return getClassPaths().size() > 0;
  }
  
  /**
   * Gets the filter associated with this ClassPathEntry.
   * 
   * @return the filter associated with this ClassPathEntry.
   */
  public Filter<String> getFilter() {
    return filter;
  }

  /**
   * Gets the paths to jars and / or classes that this ClassPathEntry
   * contains.
   * 
   * @return the paths to jars and / or classes that this ClassPathEntry
   * contains.
   */
  public List<File> getClassPaths() {
    List<File> entries = new ArrayList<File>();
    if (path.isDirectory()) {
      // search it for jar files and add each one
      for (File child : path.listFiles()) {
        if (child.getName().endsWith(".jar"))
          entries.add(child);
      }

      boolean found = containsClasses(path);
      if (found) {
        entries.add(path);
      }

    } else if (path.getName().endsWith(".jar")) {
      entries.add(path);
    }
    
    return entries;
  }
  
  private boolean containsClasses(File dir) {
    for (File file : dir.listFiles()) {
      if (file.getName().endsWith(".class"))
        return true;
    }

    for (File file : dir.listFiles()) {
      if (file.isDirectory()) {
        boolean result = containsClasses(file);
        if (result)
          return true;
      }
    }
    return false;
  }

  /**
   * Gets the names of the classes on this path. The classes must pass the
   * filter in order to be returned.
   * 
   * @return the names of the classes on this path. The classes must pass the
   *         filter in order to be returned.
   * 
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public Iterable<String> getClassNames() throws IOException, ClassNotFoundException {
    ClassFinder finder = new ClassFinder();
    finder.addEntry(this);
    return finder.findClassNames();
  }
}
