/**
 * 
 */
package repast.simphony.statecharts.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * Filters for classes that do not have one of a set of uuids in their @GeneratedBy
 * annotations, and for svg files on a certain path.
 * 
 * @author Nick Collier
 */
public class OrphanFilter implements ToDeleteFilter {

  private static final String GENERATED_FOR = "@GeneratedFor";

  private GeneratorRecord genRecord;

  public OrphanFilter(GeneratorRecord genRecord) {
    this.genRecord = genRecord;
  }
  
  private String parseUUID(String line) {
    int index = line.indexOf(GENERATED_FOR);
    index = index + GENERATED_FOR.length();
    // get the first \" quote after GENERATED_FOR
    int start = line.indexOf("\"", index);
    if (start != -1) {
      int end = line.indexOf("\"", start + 1);
      if (end != -1) {
        System.out.println(line.substring(start + 1, end));
        return line.substring(start + 1, end);
      }
    }
    return "";
  }

  private String getUUID(IFile file) {

    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(file.getContents()));
      String line = null;
      while ((line = in.readLine()) != null) {
        if (line.contains(GENERATED_FOR)) return parseUUID(line);
      }
    } catch (IOException | CoreException ex) {
      if (in != null)
        try {
          in.close();
        } catch (IOException e) {
        }
    } finally {
      if (in != null)
        try {
          in.close();
        } catch (IOException e) {
        }
    }

    return null;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * repast.simphony.statecharts.generator.ToDeleteFilter#delete(org.eclipse
   * .core.resources.IFile)
   */
  @Override
  public boolean delete(IFile file) {
    String ext = file.getFileExtension();
    if (ext != null) {
      if (ext.equals("java") || ext.equals("groovy")) {
        String uuid = getUUID(file);
        // if uuid is null that means no @GeneratedFor was found
        // so ignore the file completely
        return uuid == null ? false : !genRecord.containsUUID(uuid);
      } else if (ext.equals("svg")) {
        System.out.println(file.getFullPath());
        return !genRecord.containsSVG(file.getFullPath());
      }
    }
    return false;
  }
}
