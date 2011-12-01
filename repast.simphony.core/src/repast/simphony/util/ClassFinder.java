package repast.simphony.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Finds all the classes on a given path(s). Optional filters can be provided to
 * refine the results.
 * 
 * @author Nick Collier
 */
public class ClassFinder {

  protected static interface Adder {
    void add(String clazz) throws ClassNotFoundException;
  }

  private static class ClassAdder implements Adder {

    Set<Class<?>> classes = new HashSet<Class<?>>();
    ClassLoader loader;

    public ClassAdder() {
      loader = this.getClass().getClassLoader();
    }

    public void add(String clazz)  {
    	try{
    		classes.add(Class.forName(clazz, false, loader));
    	}
    	catch(ClassNotFoundException e){
    		return;
    	}
    }
  }

  private static class StringAdder implements Adder {

    Set<String> classes = new HashSet<String>();
    ClassLoader loader;

    public StringAdder() {
      loader = this.getClass().getClassLoader();
    }

    public void add(String clazz) throws ClassNotFoundException {
      classes.add(clazz);
    }
  }

  private List<ClassPathEntry> entries = new ArrayList<ClassPathEntry>();

  public void addEntry(ClassPathEntry entry) {
    entries.add(entry);
  }

  public List<String> findClassNames() throws IOException, ClassNotFoundException {
    StringAdder adder = new StringAdder();
    for (ClassPathEntry entry : entries) {
      findClasses(entry, adder);
    }

    return new ArrayList<String>(adder.classes);
  }

  /**
   * Gets the classes in the currently added entries, applying any filters.
   * Assumes the entries are already on the classpath.
   * 
   * @return the classes in the currently added entries.
   * @throws ClassNotFoundException
   * @throws IOException
   */
  public List<Class<?>> findClasses() throws IOException, ClassNotFoundException {
    ClassAdder adder = new ClassAdder();
    for (ClassPathEntry entry : entries) {
      findClasses(entry, adder);
    }
    
    return new ArrayList<Class<?>>(adder.classes);
  }

  private void findClasses(ClassPathEntry entry, Adder adder) throws IOException,
      ClassNotFoundException {
    for (File path : entry.getClassPaths()) {
      if (path.exists()) {
        if (path.isDirectory()) {
          int index = path.getAbsolutePath().length() + 1;
          processDirectory(entry, path, adder, index);

        } else if (path.getName().endsWith(".jar")) {
          processJar(entry, path, adder);
        }
      }
    }
  }

  private void processDirectory(ClassPathEntry entry, File path, Adder adder, int index) throws ClassNotFoundException {
    for (File child : path.listFiles()) {
      if (child.getName().endsWith(".class")) {
        String clazz = child.getAbsolutePath();
        clazz = clazz.substring(index, clazz.length() - 6);
        clazz = clazz.replace(File.separator, ".");
        if (entry.filter(clazz))
          adder.add(clazz);
        //
      } else if (child.isDirectory()) {
        processDirectory(entry, child, adder, index);
      }
    }
  }

  protected void processJar(ClassPathEntry entry, File path, Adder adder) throws IOException, ClassNotFoundException {
    JarFile jar = new JarFile(path);
    for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements();) {
      JarEntry jEntry = entries.nextElement();
      String clazz = jEntry.getName();
      if (clazz.endsWith(".class")) {
        // name will look something like
        // org/apache/commons/cli/PosixParser.class
        clazz = clazz.replace("/", ".");
        clazz = clazz.substring(0, clazz.length() - 6);
        // System.out.println("jar clazz = " + clazz);
        if (entry.filter(clazz))
          adder.add(clazz);
        // classes.add(Class.forName(clazz, false, loader));
      }
    }
  }
}
