/**
 * 
 */
package repast.simphony.statechart.part;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.junit.Test;

import repast.simphony.statecharts.part.StatechartCodeAdder;
import repast.simphony.statecharts.part.StatechartCodeAdderFactory;

/**
 * @author Nick Collier
 */
@SuppressWarnings("restriction")
public class CodeAdderTests {

  private static String PATH = "src/sample5/relogo";
  private static String JAVA = "src/sample5/relogo/Agent.java";
  private static String J_FQN = "sample5.relogo.Agent";

  private static String GROOVY = "src/sample5/relogo/UserTurtle.groovy";
  private static String G_FQN = "sample5.relogo.UserTurtle";

  private static String PACKAGE_NAME = "anl.chart";
  private static String CLASS_NAME = "MyStatechart";

  public IProject resetProject() throws CoreException, IOException {
    IWorkspace workspace = ResourcesPlugin.getWorkspace();
    IProject project = workspace.getRoot().getProject("test");
    if (!project.exists()) {
      project.create(null);
      project.open(null);

      IProjectDescription description = project.getDescription();
      description.setNatureIds(new String[] { JavaCore.NATURE_ID });
      project.setDescription(description, null);

      IFolder binFolder = project.getFolder("bin");
      binFolder.create(false, true, null);
      IJavaProject jp = JavaCore.create(project);
      jp.setOutputLocation(binFolder.getFullPath(), null);

      if (!project.getFolder("src").exists()) {
        project.getFolder("src").create(true, true, null);
      }

      IClasspathEntry[] newEntries = new IClasspathEntry[1];
      newEntries[0] = JavaCore.newSourceEntry(new Path("/test/src"));
      jp.setRawClasspath(newEntries, null);

      IFolder folder = project.getFolder(PATH);
      if (folder.exists()) {
        folder.delete(true, null);
      }

      if (!folder.getParent().exists()) {
        project.getFolder("src/sample5").create(true, true, null);
      }
      folder.create(true, true, null);

      copyFile(project, "Agent.java");
      copyFile(project, "UserTurtle.groovy");
    }

    return project;
  }

  private void copyFile(IProject project, String name) throws IOException, CoreException {
    URL relativeURL = FileLocator.find(Platform.getBundle("repast.simphony.statecharts.diagram"),
        new Path(""), null);
    URL absoluteURL = FileLocator.resolve(relativeURL);
    String tempURLString = URLDecoder.decode(absoluteURL.getFile(),
        System.getProperty("file.encoding"));
    String path = new File(tempURLString).getPath();
    IPath p = new Path(path + "/test-data/" + name);

    File from = p.toFile();
    IFile file = project.getFile(PATH + "/" + name);
    file.create(new FileInputStream(from), true, null);
  }

  @Test
  public void testGroovy() throws Exception {
    IProject proj = resetProject();
    IFile file = proj.getFile(GROOVY);
    ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
    System.out.println(unit);
    StatechartCodeAdder adder = StatechartCodeAdderFactory.createCodeAdder(unit, null);
    assertNotNull(adder);

    adder.run("statechart", PACKAGE_NAME, CLASS_NAME, G_FQN);
    checkIfAdded(file);
  }

  @Test
  public void testJava() throws Exception {
    IProject proj = resetProject();
    IFile file = proj.getFile(JAVA);
    ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
    StatechartCodeAdder adder = StatechartCodeAdderFactory.createCodeAdder(unit, null);
    assertNotNull(adder);

    adder.run("statechart", PACKAGE_NAME, CLASS_NAME, J_FQN);
    checkIfAdded(file);
  }

  private void checkIfAdded(IFile file) throws IOException {
	String fieldName = CLASS_NAME.substring(0, 1).toLowerCase()
            + CLASS_NAME.substring(1);
    assertTrue(contains(file, "import " + PACKAGE_NAME + "." + CLASS_NAME));
    String expected = CLASS_NAME + " " + fieldName + " = " + CLASS_NAME + ".createStateChart(this, 0);";
    assertTrue(contains(file, expected));
    expected = "@ProbedProperty(displayName=\"statechart\")";
    assertTrue(contains(file, expected));
    expected = "public String get" + CLASS_NAME + "State(){";
    assertTrue(contains(file, expected));
    expected = "if (" + fieldName + " == null) return \"\";";
    assertTrue(contains(file, expected));
    expected = "Object result = " + fieldName + ".getCurrentSimpleState();";
    assertTrue(contains(file, expected));
    expected = "return result == null ? \"\" : result.toString();";
    assertTrue(contains(file, expected));
  }

  private boolean contains(IFile file, String match) throws IOException {
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(new File(file.getLocation().toOSString())));
      String line = null;
      while ((line = reader.readLine()) != null) {
//    	  System.out.println("Line is: " + line); // For testing the test purposes
        if (line.contains(match))
          return true;
      }
    } finally {
      if (reader != null)
        reader.close();
    }

    return false;
  }

}
