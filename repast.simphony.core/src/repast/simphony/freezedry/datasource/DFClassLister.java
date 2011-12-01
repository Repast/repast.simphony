package repast.simphony.freezedry.datasource;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Given a zip file containing a freezedried model,
 * this returns the classes required by that model.
 *
 * @author Nick Collier
 */
public class DFClassLister {

  private String zipFileName;

  public DFClassLister(String zipFileName) {
    this.zipFileName = zipFileName;
  }

  /**
   * Gets the classes referenced in the zip file.
   *
   * @return the classes referenced in the zip file.
   * @throws IOException if there is an error reading the zip file
   * @throws ClassNotFoundException if a class referenced in the zip
   * cannot be found
   */
  public List<Class<?>> getClasses() throws ClassNotFoundException, IOException {

    List<Class<?>> classList = new ArrayList<Class<?>>();

    File file = new File(zipFileName);
    ZipFile zipFile = new ZipFile(file);

    for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
      ZipEntry zipEntry = (ZipEntry) e.nextElement();
      String entryName = zipEntry.getName();
      String name = getClassName(entryName);
      if (!entryName.startsWith(".") && !entryName.endsWith(DelimitedFileDataSource.CHILDREN_MARKER)) {
        Class<?> clazz;
        if (name.equals("[D")) {
          clazz = double[].class;
        } else if (name.equals("[I")) {
          clazz = int[].class;
        } else if (name.equals("[J")) {
          clazz = long[].class;
        } else if (name.startsWith("[L")) {
          Class compClass = Class.forName(name.substring(2, name.length() - 1));
          clazz = Array.newInstance(compClass, 1).getClass();
        } else {
          clazz = getClass().getClassLoader().loadClass(name);
        }
        classList.add(clazz);
      }
    }
    return classList;
  }

  protected String getClassName(String name) {
    if (name.endsWith(".csv")) return name.substring(0, name.length() - 4);
    return name;
  }
}

