package repast.simphony.statecharts.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class CodeGenFilter implements ToDeleteFilter {

  private String match;
  private String svgPath;

  public CodeGenFilter(String svgPath, String uuid) {
    this.match = "@GeneratedFor(\"" + uuid + "\")";
    this.svgPath = svgPath;
  }

  private boolean doDelete(IFile file) {
    
    BufferedReader in = null;
    try {
      in = new BufferedReader(new InputStreamReader(file.getContents()));
      String line = null;
      while ((line = in.readLine()) != null) {
        if (line.contains(match))
          return true;
      }
    } catch (IOException | CoreException ex) {
      if (in != null) try {
        in.close();
      } catch (IOException e) {}
    } finally {
      if (in != null) try {
        in.close();
      } catch (IOException e) {}
    }

    return false;
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
    if (ext != null && (ext.equals("java") || ext.equals("groovy"))) {
      return doDelete(file);
    }
    return file.getProjectRelativePath().toString().equals(svgPath);
  }
}
