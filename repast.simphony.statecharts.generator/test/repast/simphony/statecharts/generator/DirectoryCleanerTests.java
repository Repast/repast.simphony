/**
 * 
 */
package repast.simphony.statecharts.generator;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.junit.Test;

/**
 * THIS HAS TO BE RUN AS A JUNIT PLUGIN-TEST AS IT USES WORKSPACE ETC. 
 * 
 * @author Nick Collier
 */
public class DirectoryCleanerTests {
  
  private static String UUID = "_L8QJ0GPqEeK6zsoQEt4knA";
  
  public IProject resetFolder() throws CoreException, IOException {
    IWorkspace workspace =  ResourcesPlugin.getWorkspace();
    IProject project = workspace.getRoot().getProject("test");
    if (!project.exists()) project.create(null);
    project.open(null);
    
    IFolder folder = project.getFolder("test_out");
    if (folder.exists()) folder.delete(true, null);
    folder.create(true, true, null);
    
    return project;
  }
    
 private void copyFile(IProject project, String fromName, String toName) throws IOException, CoreException {
    URL relativeURL = FileLocator.find(Platform.getBundle("repast.simphony.statecharts.generator"), new Path(""), null);
    URL absoluteURL = FileLocator.resolve(relativeURL);
    String tempURLString = URLDecoder.decode(absoluteURL.getFile(), System.getProperty("file.encoding"));
    String path = new File(tempURLString).getPath();
    IPath p = new Path(path + "/test_data/" + fromName);
    
    File from = p.toFile();
    IFile file = project.getFile("test_out/" + toName);
    file.create(new FileInputStream(from), true, null);
  }

  @Test
  public void testJava() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder("test_out");
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    
    DirectoryCleaner cleaner = new DirectoryCleaner();
    cleaner.run(proj.getLocation().toFile().getAbsolutePath(), UUID);
    
    assertTrue(!folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }
  
  @Test
  public void testGroovy() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder("test_out");
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    
    DirectoryCleaner cleaner = new DirectoryCleaner();
    cleaner.run(proj.getLocation().toFile().getAbsolutePath(), UUID);
    
    assertTrue(!folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }
  
  @Test
  public void testBoth() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder("test_out");
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Y.java").exists());
    
    DirectoryCleaner cleaner = new DirectoryCleaner();
    cleaner.run(proj.getLocation().toFile().getAbsolutePath(), UUID);
    
    assertTrue(!folder.getFile("X.groovy").exists());
    assertTrue(!folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }
  
  @Test
  public void testUUID() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder("test_out");
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Y.java").exists());
    
    DirectoryCleaner cleaner = new DirectoryCleaner();
    cleaner.run(proj.getLocation().toFile().getAbsolutePath(), "ab");
    
    // bad uuid so all should exist
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }
}
