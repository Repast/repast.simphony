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

import repast.simphony.eclipse.util.DirectoryCleaner;

/**
 * THIS HAS TO BE RUN AS A JUNIT PLUGIN-TEST AS IT USES WORKSPACE ETC.
 * 
 * @author Nick Collier
 */
public class DirectoryCleanerTests {

  private static String UUID = "_L8QJ0GPqEeK6zsoQEt4knA";
  private static String PATH = "src-gen/anl/chart";
  private static String SVG_PATH = "src-gen/anl/chart/Chart.svg";

  public IProject resetFolder() throws CoreException, IOException {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IProject project = workspace.getRoot().getProject("test");
    if (!project.exists())
      project.create(null);
    project.open(null);

    IFolder folder = project.getFolder(PATH);
    if (folder.exists()) {
      folder.delete(true, null);
    }

    if (!folder.getParent().exists()) {
      project.getFolder("src-gen").create(true, true, null);
      project.getFolder("src-gen/anl").create(true, true, null);
    }
    folder.create(true, true, null);

    return project;
  }

  private void copyFile(IProject project, String fromName, String toName) throws IOException,
      CoreException {
    URL relativeURL = FileLocator.find(Platform.getBundle("repast.simphony.statecharts.generator"),
        new Path(""), null);
    URL absoluteURL = FileLocator.resolve(relativeURL);
    String tempURLString = URLDecoder.decode(absoluteURL.getFile(),
        System.getProperty("file.encoding"));
    String path = new File(tempURLString).getPath();
    IPath p = new Path(path + "/test_data/" + fromName);

    File from = p.toFile();
    IFile file = project.getFile(PATH + "/" + toName);
    file.create(new FileInputStream(from), true, null);
  }

  @Test
  public void testJava() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());

    CodeGenFilter filter = new CodeGenFilter(SVG_PATH, UUID);
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    assertTrue(!folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }

  @Test
  public void testOrphanFilter() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "Z.j", "Z.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    copyFile(proj, "Chart.svg", "Chart.svg");

    assertTrue(folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Chart.svg").exists());
    assertTrue(folder.getFile("Z.java").exists());

    GeneratorRecord genRecord = new GeneratorRecord();
    // Y.java should stay because the UUID is in the gen record
    genRecord.addUUID(UUID);
    genRecord.addSVG(proj.getFullPath().append(new Path(SVG_PATH)));
    OrphanFilter filter = new OrphanFilter(genRecord);
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    assertTrue(folder.getFile("Y.java").exists());
    // Z.java has no generated for so should be ignored
    assertTrue(folder.getFile("Z.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Chart.svg").exists());

    genRecord = new GeneratorRecord();
    // Y.java should be deleted because the UUID is not in the
    // genRecord
    genRecord.addUUID("ab");
    genRecord.addSVG(proj.getFullPath().append(new Path("foo.svg")));
    filter = new OrphanFilter(genRecord);
    cleaner = new DirectoryCleaner(filter);
    cleaner.run(rootPath);

    assertTrue(!folder.getFile("Y.java").exists());
    // Z.java has no generated for so should be ignored
    assertTrue(folder.getFile("Z.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(!folder.getFile("Chart.svg").exists());
  }

  @Test
  public void testGroovy() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());

    CodeGenFilter filter = new CodeGenFilter(SVG_PATH, UUID);
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    assertTrue(!folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }

  @Test
  public void testSVG() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "Chart.svg", "Chart.svg");
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "dummy.txt", "dummy.txt");

    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Chart.svg").exists());

    CodeGenFilter filter = new CodeGenFilter(SVG_PATH, UUID);
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    assertTrue(!folder.getFile("X.groovy").exists());
    assertTrue(!folder.getFile("Chart.svg").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }

  @Test
  public void testBoth() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Y.java").exists());

    CodeGenFilter filter = new CodeGenFilter(SVG_PATH, UUID);
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    assertTrue(!folder.getFile("X.groovy").exists());
    assertTrue(!folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }

  @Test
  public void testUUID() throws IOException, CoreException {
    IProject proj = resetFolder();
    IFolder folder = proj.getFolder(PATH);
    copyFile(proj, "X.g", "X.groovy");
    copyFile(proj, "Y.j", "Y.java");
    copyFile(proj, "dummy.txt", "dummy.txt");
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
    assertTrue(folder.getFile("Y.java").exists());

    CodeGenFilter filter = new CodeGenFilter(SVG_PATH, "ab");
    DirectoryCleaner cleaner = new DirectoryCleaner(filter);
    String rootPath = proj.getLocation().append("src-gen").toFile().getAbsolutePath();
    cleaner.run(rootPath);

    // bad uuid so all should exist
    assertTrue(folder.getFile("X.groovy").exists());
    assertTrue(folder.getFile("Y.java").exists());
    assertTrue(folder.getFile("dummy.txt").exists());
  }
}
